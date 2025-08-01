package com.foodon.foodon.food.domain;

import lombok.Getter;

@Getter
public enum NutrientClaimType {
    HIGH_PROTEIN(NutrientCode.PROTEIN, NutrientLevel.HIGH),
    LOW_SUGAR(NutrientCode.SUGAR, NutrientLevel.LOW),
    LOW_SODIUM(NutrientCode.SODIUM, NutrientLevel.LOW),
    HIGH_KCAL(NutrientCode.KCAL, NutrientLevel.HIGH),
    LOW_KCAL(NutrientCode.KCAL, NutrientLevel.LOW);

    private final NutrientCode nutrientCode;
    private final NutrientLevel level;

    NutrientClaimType(NutrientCode nutrientCode, NutrientLevel level) {
        this.nutrientCode = nutrientCode;
        this.level = level;
    }
}
