package com.foodon.foodon.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorCode {
    private final String code;
    private final String message;
}

