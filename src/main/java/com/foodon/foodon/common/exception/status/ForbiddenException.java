package com.foodon.foodon.common.exception.status;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends GlobalException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }
}

