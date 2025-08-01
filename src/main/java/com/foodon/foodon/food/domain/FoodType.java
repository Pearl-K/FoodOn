package com.foodon.foodon.food.domain;

import java.util.List;

public enum FoodType {
    PUBLIC, CUSTOM, CUSTOM_MODIFIED
    ;

    public static List<FoodType> customTypes() {
        return List.of(CUSTOM, CUSTOM_MODIFIED);
    }
}
