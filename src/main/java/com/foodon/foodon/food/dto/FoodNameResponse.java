package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Food;

public record FoodNameResponse(
        Long foodId,
        String foodName
) {
    public static FoodNameResponse from(Food food) {
        return new FoodNameResponse(
                food.getId(),
                food.getDisplayName().replace("_", " ")
        );
    }
}
