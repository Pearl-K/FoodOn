package com.foodon.foodon.common.exception.status;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class ConflictException extends GlobalException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.CONFLICT);
    }
}

