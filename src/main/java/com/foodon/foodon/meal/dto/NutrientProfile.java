package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientCode;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Map;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;

public record NutrientProfile(
        @PositiveOrZero(message = "칼로리는 0 이상이어야 합니다.")
        BigDecimal kcal,

        @PositiveOrZero(message = "탄수화물은 0 이상이어야 합니다.")
        BigDecimal carbs,

        @PositiveOrZero(message = "당류는 0 이상이어야 합니다.")
        BigDecimal sugar,

        @PositiveOrZero(message = "식이섬유는 0 이상이어야 합니다.")
        BigDecimal fiber,

        @PositiveOrZero(message = "단백질은 0 이상이어야 합니다.")
        BigDecimal protein,

        @PositiveOrZero(message = "지방은 0 이상이어야 합니다.")
        BigDecimal fat,

        @PositiveOrZero(message = "포화지방은 0 이상이어야 합니다.")
        BigDecimal saturatedFat,

        @PositiveOrZero(message = "트랜스지방은 0 이상이어야 합니다.")
        BigDecimal transFat,

        @PositiveOrZero(message = "지방산은 0 이상이어야 합니다.")
        BigDecimal fattyAcid,

        @PositiveOrZero(message = "불포화지방은 0 이상이어야 합니다.")
        BigDecimal unsaturatedFat,

        @PositiveOrZero(message = "콜레스테롤은 0 이상이어야 합니다.")
        BigDecimal cholesterol,

        @PositiveOrZero(message = "나트륨은 0 이상이어야 합니다.")
        BigDecimal sodium,

        @PositiveOrZero(message = "칼륨은 0 이상이어야 합니다.")
        BigDecimal potassium,

        @PositiveOrZero(message = "알코올은 0 이상이어야 합니다.")
        BigDecimal alcohol
) {
    public static NutrientProfile from(Map<NutrientCode, BigDecimal> nutrientMap) {
        return new NutrientProfile(
                round(getOrZero(nutrientMap, NutrientCode.KCAL), 0),
                round(getOrZero(nutrientMap, NutrientCode.CARBS), 1),
                round(getOrZero(nutrientMap, NutrientCode.SUGAR), 1),
                round(getOrZero(nutrientMap, NutrientCode.FIBER), 1),
                round(getOrZero(nutrientMap, NutrientCode.PROTEIN), 1),
                round(getOrZero(nutrientMap, NutrientCode.FAT), 1),
                round(getOrZero(nutrientMap, NutrientCode.SATURATED_FAT), 1),
                round(getOrZero(nutrientMap, NutrientCode.TRANS_FAT), 1),
                round(getOrZero(nutrientMap, NutrientCode.FATTY_ACID), 1),
                round(getOrZero(nutrientMap, NutrientCode.UNSATURATED_FAT), 1),
                round(getOrZero(nutrientMap, NutrientCode.CHOLESTEROL), 1),
                round(getOrZero(nutrientMap, NutrientCode.SODIUM), 1),
                round(getOrZero(nutrientMap, NutrientCode.POTASSIUM), 1),
                round(getOrZero(nutrientMap, NutrientCode.ALCOHOL), 1)
        );
    }

    public Map<NutrientCode, BigDecimal> toMap() {
        return Map.ofEntries(
                Map.entry(NutrientCode.KCAL, this.kcal),
                Map.entry(NutrientCode.CARBS, this.carbs),
                Map.entry(NutrientCode.SUGAR, this.sugar),
                Map.entry(NutrientCode.FIBER, this.fiber),
                Map.entry(NutrientCode.PROTEIN, this.protein),
                Map.entry(NutrientCode.FAT, this.fat),
                Map.entry(NutrientCode.SATURATED_FAT, this.saturatedFat),
                Map.entry(NutrientCode.TRANS_FAT, this.transFat),
                Map.entry(NutrientCode.FATTY_ACID, this.fattyAcid),
                Map.entry(NutrientCode.UNSATURATED_FAT, this.unsaturatedFat),
                Map.entry(NutrientCode.CHOLESTEROL, this.cholesterol),
                Map.entry(NutrientCode.SODIUM, this.sodium),
                Map.entry(NutrientCode.POTASSIUM, this.potassium),
                Map.entry(NutrientCode.ALCOHOL, this.alcohol)
        );
    }

    private static BigDecimal getOrZero(Map<NutrientCode, BigDecimal> map, NutrientCode code) {
        return map.getOrDefault(code, BigDecimal.ZERO);
    }

}
