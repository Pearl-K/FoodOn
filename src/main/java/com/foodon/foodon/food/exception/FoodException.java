package com.foodon.foodon.food.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;
import com.foodon.foodon.common.exception.status.ConflictException;
import com.foodon.foodon.common.exception.status.NotFoundException;
import com.foodon.foodon.meal.exception.MealErrorCode;

public class FoodException {

    public static class FoodNotFoundException extends NotFoundException {
        public FoodNotFoundException(FoodErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class FoodBadRequestException extends BadRequestException {
        public FoodBadRequestException(FoodErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class FoodConflictException extends ConflictException {
        public FoodConflictException(FoodErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
