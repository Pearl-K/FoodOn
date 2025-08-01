package com.foodon.foodon.intakelog.application;

import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.dto.IntakeDetailResponse;
import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.intakelog.exception.IntakeLogException.IntakeLogBadRequestException;
import com.foodon.foodon.intakelog.infrastructure.cache.IntakeLogCacheManager;
import com.foodon.foodon.intakelog.repository.IntakeLogRepository;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.member.domain.ActivityLevel;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.domain.NutrientPlan;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.exception.MemberException;
import com.foodon.foodon.member.repository.ActivityLevelRepository;
import com.foodon.foodon.member.repository.MemberStatusRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.repository.NutrientPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.foodon.foodon.intakelog.exception.IntakeLogErrorCode.*;

@Service
@RequiredArgsConstructor
public class IntakeLogService {

    private final IntakeLogRepository intakeLogRepository;
    private final NutrientPlanRepository nutrientPlanRepository;
    private final ActivityLevelRepository activityLevelRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final IntakeLogCacheManager cacheManager;

    /**
     * 식사 정보를 기반으로 섭취 기록을 생성하거나 갱신합니다.
     *
     * @param member 식사한 회원
     * @param meal 섭취한 식사 정보
     */
    @Transactional
    public void saveIntakeLog(Member member, Meal meal) {
        LocalDate date = meal.getMealTime().toLocalDate();
        IntakeLog intakeLog = findOrCreateIntakeLog(member, date);
        intakeLog.updateIntakeFromMeal(meal);
        intakeLogRepository.save(intakeLog);

        YearMonth yearMonth = YearMonth.from(date);
        IntakeSummaryResponse updatedResponse = getIntakeSummaryByTargetDate(date, member);
        cacheManager.putDay(member.getId().toString(), yearMonth, date, updatedResponse);
    }

    private IntakeLog findOrCreateIntakeLog(Member member, LocalDate date) {
        return findIntakeLogByDate(member, date)
                .orElseGet(() -> createIntakeLog(member, date));
    }

    private IntakeLog createIntakeLog(Member member, LocalDate date) {
        MemberStatus latestStatus = getLatestStatusOrThrow(member);
        ActivityLevel activityLevel = findActivityLevelById(latestStatus.getActivityLevelId());
        BigDecimal goalKcal = NutrientGoal.calculateGoalKcal(member, latestStatus, activityLevel);
        return IntakeLog.createIntakeLogOfMember(member, date, goalKcal);
    }

    /**
     * 월별 섭취 요약 정보를 조회합니다.
     * 각 날짜별로 섭취 기록이 존재하면 해당 기록을,
     * 없으면 당일 목표 칼로리를 기반으로 빈 기록을 반환합니다.
     *
     * @param yearMonth 조회할 연월
     * @param member 조회 대상 회원
     * @return 해당 월의 섭취 요약 리스트
     */
    public List<IntakeSummaryResponse> getIntakeLogCalendar(
            YearMonth yearMonth,
            Member member
    ) {
        String userId = member.getId().toString();
        List<LocalDate> allDates = getAllDatesInMonth(yearMonth);

        Map<LocalDate, IntakeSummaryResponse> cachedLogs = cacheManager.getCachedMonth(userId, yearMonth);

        List<LocalDate> missingDates = allDates.stream()
                .filter(date -> !cachedLogs.containsKey(date))
                .toList();

        Map<LocalDate, IntakeSummaryResponse> calculatedLog = missingDates.isEmpty()
                ? Map.of()
                : calculateMissingDateLogs(missingDates, yearMonth, member);

        return allDates.stream()
                .map(date -> cachedLogs.containsKey(date)
                        ? cachedLogs.get(date)
                        : calculatedLog.get(date))
                .toList();
    }

    private Map<LocalDate, IntakeSummaryResponse> calculateMissingDateLogs(
            List<LocalDate> missingDates,
            YearMonth yearMonth,
            Member member
    ) {
        TreeMap<LocalDate, MemberStatus> recordMap = findLatestMemberStatusSortedMap(member, yearMonth);
        Map<Long, ActivityLevel> activityLevelMap = findAllActivityLevels();
        Map<LocalDate, IntakeLog> intakeLogMap = findIntakeLogsInMonth(member, yearMonth);

        Map<LocalDate, IntakeSummaryResponse> result = new HashMap<>();
        String userId = member.getId().toString();

        for (LocalDate date : missingDates) {
            IntakeSummaryResponse summaryResponse = convertToIntakeSummaryResponse(
                    date, member, intakeLogMap, recordMap, activityLevelMap
            );
            result.put(date, summaryResponse);
            cacheManager.putDay(userId, yearMonth, date, summaryResponse);
        }

        return result;
    }

    private Map<Long, ActivityLevel> findAllActivityLevels() {
        return activityLevelRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ActivityLevel::getId, Function.identity()));
    }

    private IntakeSummaryResponse convertToIntakeSummaryResponse(
            LocalDate date,
            Member member,
            Map<LocalDate, IntakeLog> intakeLogMap,
            TreeMap<LocalDate, MemberStatus> recordMap,
            Map<Long, ActivityLevel> activityLevelMap
    ) {
        if (intakeLogMap.containsKey(date)) {
            return IntakeSummaryResponse.withIntakeLog(intakeLogMap.get(date));
        }

        MemberStatus status = getLatestMemberStatusBeforeDate(recordMap, date);
        BigDecimal goalKcal = (!Objects.isNull(status) && activityLevelMap.containsKey(status.getActivityLevelId()))
                ? NutrientGoal.calculateGoalKcal(member, status, activityLevelMap.get(status.getActivityLevelId()))
                : BigDecimal.ZERO;

        return IntakeSummaryResponse.withoutIntakeLog(goalKcal, date);
    }

    private MemberStatus getLatestMemberStatusBeforeDate(
            TreeMap<LocalDate, MemberStatus> recordMap,
            LocalDate date
    ) {
        return recordMap.getOrDefault(date,
                Optional.ofNullable(recordMap.floorEntry(date))
                        .map(Map.Entry::getValue)
                        .orElse(null)
        );
    }

    private List<LocalDate> getAllDatesInMonth(YearMonth yearMonth) {
        return IntStream.rangeClosed(1, yearMonth.lengthOfMonth())
                .mapToObj(yearMonth::atDay)
                .toList();
    }

    private TreeMap<LocalDate, MemberStatus> findLatestMemberStatusSortedMap(
            Member member,
            YearMonth yearMonth
    ) {
        LocalDate start = member.getCreatedAt().toLocalDate();
        LocalDate end = yearMonth.atEndOfMonth();

        return memberStatusRepository.findByMemberIdAndCreatedAtBetweenOrderByCreatedAt(
                member.getId(),
                start,
                end
        ).stream().collect(Collectors.toMap(
                MemberStatus::getCreatedAt,
                Function.identity(),
                (prev, next) -> prev,
                TreeMap::new
        ));
    }

    private Map<LocalDate, IntakeLog> findIntakeLogsInMonth(
            Member member,
            YearMonth yearMonth
    ) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return intakeLogRepository.findByMemberAndDateBetween(member, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(IntakeLog::getDate, Function.identity()));
    }

    private void validateYearMonthFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IntakeLogBadRequestException(ILLEGAL_DATE_FORMAT);
        }
    }

    /**
     * 특정 날짜의 섭취 상세 정보를 조회합니다.
     * 섭취 기록이 있으면 실제 기록 데이터를, 없으면 목표 섭취량만 반환합니다.
     *
     * @param date 조회할 날짜
     * @param member 조회 대상 회원
     * @return 섭취 상세 응답 DTO
     */
    public IntakeDetailResponse getIntakeDailyDetail(
            LocalDate date,
            Member member
    ) {
        return findIntakeLogByDate(member, date)
                .map(intakeLog -> {
                    MemberStatus latestStatus = getLatestStatusOrThrow(member);
                    NutrientPlan nutrientPlan = findNutrientPlanById(latestStatus.getNutrientPlanId());
                    NutrientGoal nutrientGoal = NutrientGoal.from(intakeLog.getGoalKcal(), nutrientPlan);
                    return IntakeDetailResponse.withIntakeLog(nutrientGoal, intakeLog, date);
                })
                .orElseGet(() -> {
                    NutrientGoal nutrientGoal = getNutrientGoalFromMemberStatus(member);
                    return IntakeDetailResponse.withOutIntakeLog(nutrientGoal, date);
                });
    }

    /**
     * 특정 날짜의 섭취 요약 정보를 조회합니다.
     * 섭취 기록이 있으면 해당 데이터를, 없으면 목표 칼로리만 포함한 빈 기록을 반환합니다.
     *
     * @param date 조회할 날짜
     * @param member 조회 대상 회원
     * @return 섭취 요약 응답 DTO
     */
    public IntakeSummaryResponse getIntakeSummaryByTargetDate(LocalDate date, Member member) {
        return findIntakeLogByDate(member, date)
                .map(IntakeSummaryResponse::withIntakeLog)
                .orElseGet(() -> {
                    BigDecimal goalKcal = getGoalKcalFromMemberStatus(member);
                    return IntakeSummaryResponse.withoutIntakeLog(goalKcal, date);
                });
    }

    private NutrientGoal getNutrientGoalFromMemberStatus(Member member) {
        MemberStatus latestStatus = getLatestStatusOrThrow(member);
        ActivityLevel activityLevel = findActivityLevelById(latestStatus.getActivityLevelId());
        NutrientPlan nutrientPlan = findNutrientPlanById(latestStatus.getNutrientPlanId());
        return NutrientGoal.from(member, latestStatus, activityLevel, nutrientPlan);
    }

    private BigDecimal getGoalKcalFromMemberStatus(Member member) {
        MemberStatus latestStatus = getLatestStatusOrThrow(member);
        ActivityLevel activityLevel = findActivityLevelById(latestStatus.getActivityLevelId());
        return NutrientGoal.calculateGoalKcal(member, latestStatus, activityLevel);
    }

    private NutrientPlan findNutrientPlanById(Long nutrientPlanId) {
        return nutrientPlanRepository.findById(nutrientPlanId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 관리 유형이 존재하지 않습니다. id = " + nutrientPlanId));
    }

    private Optional<IntakeLog> findIntakeLogByDate(Member member, LocalDate date) {
        return intakeLogRepository.findByMemberAndDate(member, date);
    }

    private ActivityLevel findActivityLevelById(Long activityLevelId) {
        return activityLevelRepository.findById(activityLevelId)
                .orElseThrow(() -> new NoSuchElementException(("해당 ID의 활동량 유형이 존재하지 않습니다. activityLevelId = " + activityLevelId)));
    }

    private MemberStatus getLatestStatusOrThrow(Member member) {
        return memberStatusRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
                .orElseThrow(
                        () -> new MemberException.MemberBadRequestException(MemberErrorCode.MEMBER_STATUS_NOT_FOUND)
                );
    }
}
