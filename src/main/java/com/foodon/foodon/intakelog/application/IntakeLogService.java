package com.foodon.foodon.intakelog.application;

import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.dto.IntakeDetailResponse;
import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.intakelog.exception.IntakeLogException.IntakeLogBadRequestException;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
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


    @Transactional
    @CacheEvict(
            value = "intakeCalendar",
            key = "'calendar:' + #member.id + ':' + T(java.time.YearMonth).from(#meal.mealTime)"
    )
    public void saveIntakeLog(Member member, Meal meal) {
        LocalDate date = meal.getMealTime().toLocalDate();
        IntakeLog intakeLog = findOrCreateIntakeLog(member, date);
        intakeLog.updateIntakeFromMeal(meal);
        intakeLogRepository.save(intakeLog);
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

    @Cacheable(value = "intakeCalendar", key = "'calendar:' + #member.id + ':' + #yearMonth")
    public List<IntakeSummaryResponse> getIntakeLogCalendar(
            YearMonth yearMonth,
            Member member
    ) {
        List<LocalDate> allDates = getAllDatesInMonth(yearMonth);
        TreeMap<LocalDate, MemberStatus> recordMap = findLatestMemberStatusSortedMap(member, yearMonth);
        Map<Long, ActivityLevel> activityLevelMap = findAllActivityLevels();
        Map<LocalDate, IntakeLog> intakeLogMap = findIntakeLogsInMonth(member, yearMonth);

        return allDates.stream()
                .map(date -> convertToIntakeSummaryResponse(date, member, intakeLogMap, recordMap, activityLevelMap))
                .toList();
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

    private Map<LocalDate, IntakeLog> findIntakeLogsInMonth(Member member, YearMonth yearMonth) {
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

    public IntakeSummaryResponse getIntakeLogByTargetDate(LocalDate date, Member member) {
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
