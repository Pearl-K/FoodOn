package com.foodon.foodon.common.exception.status;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends GlobalException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }
}
