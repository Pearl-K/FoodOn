package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.NutrientClaimType;

public record NutrientClaimInfo(
        Long foodId,
        Long nutrientClaimId,
        String name,
        NutrientClaimType type
) {
}
