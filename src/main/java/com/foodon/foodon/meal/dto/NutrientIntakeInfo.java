package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;

public record NutrientIntakeInfo(
        Long nutrientId,
        String nutrientName,
        NutrientCode nutrientCode,
        BigDecimal intake
) {
}
