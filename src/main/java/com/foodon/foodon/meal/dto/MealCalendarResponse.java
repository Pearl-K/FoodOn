package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.presentation.MealController;

import java.time.LocalDate;
import java.util.List;

public record MealCalendarResponse(
        LocalDate date,
        List<MealThumbnailInfoResponse> meals
) {
    public static MealCalendarResponse from(
            LocalDate date,
            List<MealThumbnailInfo> mealThumbnailInfo
    ){
        return new MealCalendarResponse(
                date,
                mealThumbnailInfo.stream()
                        .map(MealThumbnailInfoResponse::of)
                        .toList()
        );
    }
}
