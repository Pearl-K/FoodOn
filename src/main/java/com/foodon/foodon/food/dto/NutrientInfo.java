package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientUnit;

import java.math.BigDecimal;

public record NutrientInfo(
        Long foodId,
        Long foodNutrientId,
        String name,
        NutrientCode code,
        NutrientUnit nutrientUnit,
        BigDecimal value
) {
}
