package com.foodon.foodon.auth.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.ForbiddenException;
import com.foodon.foodon.common.exception.status.UnauthorizedException;

public class AuthException {

    public static class AuthUnauthorizedException extends UnauthorizedException {

        public AuthUnauthorizedException(AuthErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class AuthForbiddenException extends ForbiddenException {

        public AuthForbiddenException(AuthErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}

