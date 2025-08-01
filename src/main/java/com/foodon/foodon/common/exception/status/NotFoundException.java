package com.foodon.foodon.common.exception.status;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}

