package com.foodon.foodon.common.exception.status;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GlobalException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }
}

