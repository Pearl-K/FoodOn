package com.foodon.foodon.food.domain;

import lombok.Getter;

@Getter
public enum NutrientUnit {

    GRAM("g"),
    MILLIGRAM("mg"),
    KCAL("kcal");

    private final String symbol;

    NutrientUnit(String symbol) {
        this.symbol = symbol;
    }
}
