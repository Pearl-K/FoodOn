package com.foodon.foodon.recommend.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;

public class RecommendFoodException {

    public static class RecommendFoodBadRequestException extends BadRequestException{
        public RecommendFoodBadRequestException(RecommendFoodErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
