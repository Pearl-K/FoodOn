package com.foodon.foodon.intakelog.exception;

import lombok.Getter;

@Getter
public enum IntakeLogErrorCode {

    ILLEGAL_DATE_FORMAT("60000", "날짜 형식이 올바르지 않습니다. (yyyy-MM 이어야 합니다)")
    ;

    private final String code;
    private final String message;

    IntakeLogErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
