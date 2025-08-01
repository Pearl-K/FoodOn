package com.foodon.foodon.meal.domain;

import lombok.Getter;

@Getter
public enum MealTimeType {
    BREAKFAST("아침식사"),
    LUNCH("점심식사"),
    DINNER("저녁식사"),
    SNACK("간식");

    private final String description;

    MealTimeType(String description) {
        this.description = description;
    }
}
