package com.foodon.foodon.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WeightUpdateRequest(
	@NotNull
	@Min(value = 1, message = "체중은 최소 1 이상이어야 합니다")
	int weight
) {
}
