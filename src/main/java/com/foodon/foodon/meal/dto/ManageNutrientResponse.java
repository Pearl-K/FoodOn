package com.foodon.foodon.meal.dto;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.food.domain.*;
import com.foodon.foodon.meal.domain.ManageNutrient;

import java.math.BigDecimal;

public record ManageNutrientResponse(
        String nutrientName,
        NutrientCode nutrientCode,
        RestrictionType restrictionType,
        NutrientUnit unit,
        BigDecimal intake,
        BigDecimal minRecommended,
        BigDecimal maxRecommended,
        HealthEffect healthEffect,
        ManageStatus status
) {
    public static ManageNutrientResponse from(
            Nutrient nutrient,
            BigDecimal intake,
            BigDecimal minRecommended,
            BigDecimal maxRecommended,
            ManageStatus status
    ){
        return new ManageNutrientResponse(
                nutrient.getName(),
                nutrient.getCode(),
                nutrient.getRestrictionType(),
                nutrient.getNutrientUnit(),
                BigDecimalUtil.round(intake, 1),
                BigDecimalUtil.round(minRecommended, 1),
                BigDecimalUtil.round(maxRecommended, 1),
                nutrient.getHealthEffect(),
                status
        );
    }
}
