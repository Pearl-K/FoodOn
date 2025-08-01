package com.foodon.foodon.member.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;
import com.foodon.foodon.common.exception.status.ConflictException;
import com.foodon.foodon.common.exception.status.ForbiddenException;
import com.foodon.foodon.common.exception.status.NotFoundException;

public class MemberException {

    public static class MemberBadRequestException extends BadRequestException {
        public MemberBadRequestException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class MemberNotFoundException extends NotFoundException {
        public MemberNotFoundException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class MemberForbiddenException extends ForbiddenException {
        public MemberForbiddenException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class MemberConflictException extends ConflictException {
        public MemberConflictException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
