package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

public record NutrientClaimResponse(
        NutrientCode code,
        NutrientLevel level
) {
    public static NutrientClaimResponse from(
            NutrientClaimType nutrientClaimType
    ) {
        return new NutrientClaimResponse(
                nutrientClaimType.getNutrientCode(),
                nutrientClaimType.getLevel()
        );
    }
}
