package com.foodon.foodon.meal.dto;

import java.time.LocalDateTime;

public record MealThumbnailInfo(
        Long mealId,
        Long mealItemId,
        LocalDateTime mealTime,
        String foodName,
        String mealImage,
        PositionInfo positionInfo
) {
}
