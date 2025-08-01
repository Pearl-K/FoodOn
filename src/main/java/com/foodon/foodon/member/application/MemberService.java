package com.foodon.foodon.member.application;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.foodon.foodon.member.domain.ActivityLevel;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.domain.NutrientPlan;
import com.foodon.foodon.member.dto.*;
import com.foodon.foodon.member.repository.ActivityLevelRepository;
import com.foodon.foodon.member.repository.NutrientPlanRepository;

import com.foodon.foodon.member.dto.ProfileRegisterRequest;
import com.foodon.foodon.member.repository.MemberStatusRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.repository.MemberRepository;
import static com.foodon.foodon.member.exception.MemberException.MemberBadRequestException;
import static com.foodon.foodon.member.exception.MemberException.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ActivityLevelRepository activityLevelRepository;
	private final NutrientPlanRepository nutrientPlanRepository;
	private final MemberStatusRepository memberStatusRepository;

	@Transactional
	public void registerProfile(
			ProfileRegisterRequest request,
			Long memberId
	) {
		Member member = getMember(memberId);
		memberStatusRepository.save(
				MemberStatus.createMemberStatus(
						member.getId(),
						request.height(),
						request.weight(),
						request.goalWeight(),
						request.managementType(),
						request.activityType()
				)
		);

		member.updateProfile(request.gender());
		member.markProfileUpdated();
	}

	public List<WeightRecordResponse> getWeightRecordCalendar(
			YearMonth yearMonth,
			Member member
	) {
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<MemberStatus> records = memberStatusRepository.findByMemberIdAndCreatedAtBetweenOrderByCreatedAt(
				member.getId(),
				start,
				end
		);

		return convertToResponse(records);
	}

	private List<WeightRecordResponse> convertToResponse(List<MemberStatus> records) {
		return records.stream()
				.map(WeightRecordResponse::of)
				.collect(Collectors.toList());
	}

	public WeightProfileResponse getWeightProfile(Member member) {
		MemberStatus latestStatus = getLatestStatusOrThrow(member);

		return WeightProfileResponse.of(latestStatus);
	}

	@Transactional
	public void updateCurrentWeight(
			Member member,
			WeightUpdateRequest weightUpdateRequest
	) {
		int currentWeight = weightUpdateRequest.weight();
		LocalDate today = LocalDate.now();

		MemberStatus latestStatus = getLatestStatusOrThrow(member);

		if (latestStatus.getCreatedAt().isEqual(today)) {
			latestStatus.updateWeight(currentWeight);
		}else {
			MemberStatus newStatus = MemberStatus.createFromPrevious(
					latestStatus,
					currentWeight,
					today
			);
			memberStatusRepository.save(newStatus);
		}
	}

	public List<ActivityLevelResponse> getActivityLevels() {
		List<ActivityLevel> activityLevels = activityLevelRepository.findAll();
		return activityLevels.stream()
				.map(ActivityLevelResponse::of)
				.collect(Collectors.toList());
	}

	public List<NutrientPlanResponse> getNutrientPlans() {
		List<NutrientPlan> nutrientPlans = nutrientPlanRepository.findAll();
		return nutrientPlans.stream()
				.map(NutrientPlanResponse::of)
				.collect(Collectors.toList());
	}

	private MemberStatus getLatestStatusOrThrow(Member member) {
		return memberStatusRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
				.orElseThrow(
						() -> new MemberBadRequestException(MemberErrorCode.MEMBER_STATUS_NOT_FOUND)
				);
	}

	@Transactional
	public void updateLastLoginTime(Long memberId) {
		Member member = getMember(memberId);
		member.updateLastLoginTime();
	}

	@Transactional(readOnly = true)
	public GoalManagementResponse getGoalManagementProfile(Member member) {
		MemberStatus status = getLatestStatusOrThrow(member);
		ActivityLevel activity = getActivityLevelOrThrow(status);
		NutrientPlan plan = getNutrientPlanOrThrow(status);

		return GoalManagementResponse.from(member, status, activity, plan);
	}

	private ActivityLevel getActivityLevelOrThrow(MemberStatus status) {
		return activityLevelRepository.findById(status.getActivityLevelId())
				.orElseThrow(() -> new MemberBadRequestException(MemberErrorCode.ACTIVITY_LEVEL_NOT_FOUND));
	}

	private NutrientPlan getNutrientPlanOrThrow(MemberStatus status) {
		return nutrientPlanRepository.findById(status.getNutrientPlanId())
				.orElseThrow(() -> new MemberBadRequestException(MemberErrorCode.NUTRIENT_PLAN_NOT_FOUND));
	}

	public Boolean getMemberProfileUpdated(Member member) {
		return member.isProfileUpdated();
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}
