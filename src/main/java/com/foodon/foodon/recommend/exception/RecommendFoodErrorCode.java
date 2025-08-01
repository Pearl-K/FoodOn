package com.foodon.foodon.recommend.exception;

import lombok.Getter;

@Getter
public enum RecommendFoodErrorCode {

    ILLEGAL_WEEK_RANGE("90000", "유효하지 않은 주차입니다.")
    ;

    private final String code;
    private final String message;

    RecommendFoodErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
