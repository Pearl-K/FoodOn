package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;

public record CustomFoodCreateResponse(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        NutrientProfile nutrientInfo
) {
    public static CustomFoodCreateResponse from(
            Food food,
            NutrientProfile nutrientInfo
    ){
        return new CustomFoodCreateResponse(
                food.getFoodType(),
                food.getId(),
                food.getDisplayName(),
                food.getUnit(),
                nutrientInfo
        );
    }
}
