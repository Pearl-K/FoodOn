package com.foodon.foodon.intakelog.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;

public class IntakeLogException {

    public static class IntakeLogBadRequestException extends BadRequestException{
        public IntakeLogBadRequestException(IntakeLogErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
