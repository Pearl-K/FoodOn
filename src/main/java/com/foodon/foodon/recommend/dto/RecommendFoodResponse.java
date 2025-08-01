package com.foodon.foodon.recommend.dto;

import com.foodon.foodon.food.dto.FoodWithNutrientClaimInfo;
import com.foodon.foodon.food.dto.NutrientClaimInfo;
import com.foodon.foodon.food.dto.NutrientClaimResponse;
import com.foodon.foodon.recommend.domain.RecommendFood;

import java.math.BigDecimal;
import java.util.List;

public record RecommendFoodResponse(
        Long foodId,
        String foodName,
        BigDecimal kcal,
        String reason,
        List<NutrientClaimResponse> nutrientClaims
) {
    public static RecommendFoodResponse from(
            RecommendFood recommendFood,
            List<NutrientClaimInfo> nutrientClaims
    ) {
        return new RecommendFoodResponse(
                recommendFood.getFoodId(),
                recommendFood.getFoodName(),
                recommendFood.getKcalPerServing(),
                recommendFood.getReason(),
                nutrientClaims.stream()
                        .map(NutrientClaimInfo::type)
                        .map(NutrientClaimResponse::from)
                        .toList()
        );
    }
}

