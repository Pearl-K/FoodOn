package com.foodon.foodon.member.presentation;

import java.time.YearMonth;
import java.util.List;

import com.foodon.foodon.member.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.member.application.MemberService;
import com.foodon.foodon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/weights/calendar/{yearMonth}")
	@Operation(summary = "특정 달 기록 체중 목록 조회")
	public ResponseEntity<Response<List<WeightRecordResponse>>> getWeightCalendar(
		@PathVariable YearMonth yearMonth,
		@Parameter(hidden = true) @AuthMember Member member
	) {
		List<WeightRecordResponse> result = memberService.getWeightRecordCalendar(yearMonth, member);
		return ResponseUtil.success(result);
	}

	@PostMapping("/profile")
	@Operation(summary = "가입한 사용자 정보 등록 (키, 체중 등)")
	public ResponseEntity<Response<Void>> registerProfile(
			@Valid @RequestBody ProfileRegisterRequest request,
			@Parameter(hidden = true) @AuthMember Member member
	){
		memberService.registerProfile(request, member.getId());
		return ResponseUtil.success();
	}

	@GetMapping("/profile/weight")
	@Operation(summary = "목표 체중 현재 체중 조회")
	public ResponseEntity<Response<WeightProfileResponse>> getWeightProfile(
		@Parameter(hidden = true) @AuthMember Member member
	) {
		WeightProfileResponse result = memberService.getWeightProfile(member);
		return ResponseUtil.success(result);
	}

	@PatchMapping("/profile/weight")
	@Operation(summary = "현재 체중 업데이트")
	public ResponseEntity<Response<WeightProfileResponse>> updateCurrentWeight(
		@Parameter(hidden = true) @AuthMember Member member,
		@Valid @RequestBody WeightUpdateRequest weightUpdateRequest
	) {
		memberService.updateCurrentWeight(member, weightUpdateRequest);
		return ResponseUtil.success();
	}

	@GetMapping("/profile/activity-levels")
	@Operation(summary = "활동량 유형 목록 조회")
	public ResponseEntity<Response<List<ActivityLevelResponse>>> getActivityLevels(
			@Parameter(hidden = true) @AuthMember Member member
	){
		List<ActivityLevelResponse> result = memberService.getActivityLevels();
		return ResponseUtil.success(result);
	}

	@GetMapping("/profile/nutrient-plans")
	@Operation(summary = "관리 유형 목록 조회")
	public ResponseEntity<Response<List<NutrientPlanResponse>>> getNutrientPlans(
			@Parameter(hidden = true) @AuthMember Member member
	){
		List<NutrientPlanResponse> result = memberService.getNutrientPlans();
		return ResponseUtil.success(result);
	}

	@PostMapping("/last-login")
	@Operation(summary = "유저 접속 시간 갱신")
	public ResponseEntity<Response<Void>> updateMemberLastLogin(
		@Parameter(hidden = true) @AuthMember Member member
	) {
		memberService.updateLastLoginTime(member.getId());
		return ResponseUtil.success();
	}

	@GetMapping("/profile/goal-management")
	@Operation(summary = "목표 관리를 위한 관리 유형과 섭취 정보, 신체 정보 제공")
	public ResponseEntity<Response<GoalManagementResponse>> getGoalManagementProfile(
			@Parameter(hidden = true) @AuthMember Member member
	) {
		GoalManagementResponse response = memberService.getGoalManagementProfile(member);
		return ResponseUtil.success(response);
	}

	@GetMapping("/me/profile/status")
	@Operation(summary = "자신의 건강 정보가 DB에 등록되어있는지 상태 확인")
	public ResponseEntity<Response<Boolean>> getMemberProfileUpdated(
			@Parameter(hidden = true) @AuthMember Member member
	) {
		Boolean isUpdated = memberService.getMemberProfileUpdated(member);
		return ResponseUtil.success(isUpdated);
	}
}
