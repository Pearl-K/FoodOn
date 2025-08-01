package com.foodon.foodon.member.dto;

import java.time.LocalDate;

import com.foodon.foodon.member.domain.MemberStatus;

public record WeightRecordResponse(
	LocalDate date,
	int weight
) {
	public static WeightRecordResponse of(MemberStatus memberStatus) {
		return new WeightRecordResponse(
			memberStatus.getCreatedAt(),
			memberStatus.getWeight()
		);
	}
}
