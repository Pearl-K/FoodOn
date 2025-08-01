package com.foodon.foodon.common.exception.dto;

import com.foodon.foodon.common.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
