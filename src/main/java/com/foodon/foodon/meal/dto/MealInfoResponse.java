package com.foodon.foodon.meal.dto;

import com.foodon.foodon.common.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;

public record MealInfoResponse(
        String imageFileName,
        BigDecimal totalKcal,
        BigDecimal totalCarbs,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        List<MealItemInfo> mealItems
) {
    public static MealInfoResponse from(
            String imageFileName,
            BigDecimal totalKcal,
            BigDecimal totalCarbs,
            BigDecimal totalProtein,
            BigDecimal totalFat,
            List<MealItemInfo> mealItems
    ) {
        return new MealInfoResponse(
                imageFileName,
                round(totalKcal, 0),
                round(totalCarbs, 1),
                round(totalProtein, 1),
                round(totalFat, 1),
                mealItems
        );
    }
}
