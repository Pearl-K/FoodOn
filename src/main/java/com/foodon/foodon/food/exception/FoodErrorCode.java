package com.foodon.foodon.food.exception;


import lombok.Getter;

@Getter
public enum FoodErrorCode {
    // custom food
    CONFLICT_CUSTOM_FOOD("50000", "이미 같은 이름으로 등록된 음식이 있습니다."),
    NOT_FOUND_CUSTOM_FOOD("50001", "해당 ID의 등록된 음식이 없습니다."),

    // public
    NOT_FOUND_PUBLIC_FOOD("50101", "해당 ID의 음식이 없습니다."),
    ILLEGAL_FOOD_TYPE("50102", "잘못된 음식 유형입니다."),
    ILLEGAL_NUTRIENT_ID("50103", "요청한 ID의 영양소는 존재하지 않습니다."),
    ILLEGAL_FOOD_NAME_BLANK("50104", "음식 이름은 공백일 수 없습니다."),
    ;

    private final String code;
    private final String message;

    FoodErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
