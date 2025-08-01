package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.MemberStatus;

public record WeightProfileResponse(
	int goalWeight,
	int currentWeight
) {

	public static WeightProfileResponse of(MemberStatus memberStatus) {
		return new WeightProfileResponse(
			memberStatus.getGoalWeight(),
			memberStatus.getWeight()
		);
	}
}
