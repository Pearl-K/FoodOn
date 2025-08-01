package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.MealTimeType;

import java.math.BigDecimal;
import java.util.List;

public record MealCreateRequest(
        String imageFileName,
        BigDecimal totalKcal,
        BigDecimal totalCarbs,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        MealTimeType mealTimeType,
        String mealTime,
        List<MealItemInfo> mealItems
) {
}
