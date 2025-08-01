package com.foodon.foodon.image.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;

public class ImageException {

    public static class ImageBadRequestException extends BadRequestException {
        public ImageBadRequestException(ImageErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
