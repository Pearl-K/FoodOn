package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;

import java.math.BigDecimal;
import java.util.Map;

public record FoodDetailInfoResponse(
        FoodType type,
        Long foodId,
        String foodName,
        BigDecimal servingSize,
        Unit unit,
        NutrientProfile nutrientInfo
) {
    public static FoodDetailInfoResponse from(
            FoodWithNutrientInfo foodWithNutrientInfo,
            Map<NutrientCode, BigDecimal> nutrientMap
    ) {
        return new FoodDetailInfoResponse(
                foodWithNutrientInfo.type(),
                foodWithNutrientInfo.foodId(),
                foodWithNutrientInfo.foodName(),
                foodWithNutrientInfo.servingSize(),
                foodWithNutrientInfo.unit(),
                NutrientProfile.from(nutrientMap)
        );
    }
}
