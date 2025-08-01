package com.foodon.foodon.common.util;

import com.foodon.foodon.food.domain.NutrientUnit;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.dto.NutrientProfile;
import com.foodon.foodon.meal.dto.MealItemInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.foodon.foodon.common.util.BigDecimalUtil.divide;
import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;


public class NutrientCalculator {

    /**
     * 영양 성분 섭취량 총합 계산
     */
    public static BigDecimal sumTotalIntake(
            List<MealItemInfo> items,
            Function<NutrientProfile, BigDecimal> nutrientGetter
    ) {
        return items.stream()
                .map(item -> {
                    BigDecimal valuePerUnit = nutrientGetter.apply(item.nutrientInfo());
                    BigDecimal quantity = item.quantity();
                    return multiply(valuePerUnit, quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal convertToMilligram(BigDecimal value, NutrientUnit unit) {
        if (value == null) return BigDecimal.ZERO;

        return unit.equals(NutrientUnit.GRAM)
                ? multiply(value, BigDecimal.valueOf(1000))
                : value;
    }

    public static BigDecimal convertToGram(BigDecimal value, NutrientUnit unit) {
        if (value == null) return BigDecimal.ZERO;

        return unit.equals(NutrientUnit.MILLIGRAM)
                ? divide(value, BigDecimal.valueOf(1000))
                : value;
    }

    /**
     * 100g 당 함량 → 1회 제공량당 영양성분 함량 계산
     */
    public static BigDecimal calculateNutrientPerServing(
            BigDecimal foodServingSize,
            BigDecimal per100g
    ){
        return divide(per100g, 10, BigDecimal.valueOf(100))
                .multiply(foodServingSize);
    }

    /**
     * 1회 제공량 기준 영양성분 함량 → 100g 기준 영양성분 함량
     */
    public static BigDecimal calculateNutrientPer100g(
            BigDecimal foodServingSize,
            BigDecimal perServing
    ) {
        return divide(perServing, 10, foodServingSize)
                .multiply(BigDecimal.valueOf(100));
    }

}
