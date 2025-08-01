package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealTimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;

public record MealDetailInfoResponse(
        Long mealId,
        String mealTime,
        MealTimeType mealTimeType,
        String imageFileName,
        BigDecimal totalKcal,
        BigDecimal totalCarbs,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        List<MealItemInfo> mealItems
) {
    public static MealDetailInfoResponse from(
            Meal meal,
            List<MealItemInfo> mealItems
    ) {
        return new MealDetailInfoResponse(
                meal.getId(),
                meal.getMealTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                meal.getMealTimeType(),
                meal.getMealImage(),
                round(meal.getTotalKcal(), 0),
                round(meal.getTotalCarbs(), 1),
                round(meal.getTotalProtein(), 1),
                round(meal.getTotalFat(), 1),
                mealItems
        );
    }
}
