package com.foodon.foodon.meal.exception;

import lombok.Getter;

@Getter
public enum MealErrorCode {

    MEAL_ITEM_IS_NULL("40000", "식단에 기록할 음식 정보가 없습니다."),
    NOT_FOUND_MEAL("40001", "회원이 등록한 식단 기록 정보가 존재하지 않습니다.")
    ;

    private final String code;
    private final String message;

    MealErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
