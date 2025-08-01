package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.domain.MealTimeType;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;
import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record MealSummaryResponse(
        Long mealId,
        String imageUrl,
        MealTimeType mealTimeType,
        String mealTime,
        BigDecimal totalKcal,
        BigDecimal totalCarbs,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        List<String> mealItems
) {
    public static MealSummaryResponse of(Meal meal) {
        return new MealSummaryResponse(
                meal.getId(),
                meal.getMealImage(),
                meal.getMealTimeType(),
                meal.getMealTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                round(meal.getTotalKcal(), 0),
                round(meal.getTotalCarbs(), 1),
                round(meal.getTotalProtein(), 1),
                round(meal.getTotalFat(), 1),
                meal.getMealItems().stream().map(MealItem::getFoodName).toList()
        );
    }
}
