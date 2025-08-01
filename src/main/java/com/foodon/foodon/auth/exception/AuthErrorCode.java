package com.foodon.foodon.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorCode {

    UNAUTHORIZED_ACCESS("20000", "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN("20001", "유효하지 않은 Refresh Token입니다."),
    FAILED_TO_VALIDATE_TOKEN("20002", "토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN("20003", "유효하지 않은 Access Token입니다."),
    CORRUPTED_OAUTH_LINK("20004", "계정 연동 오류: 관리자에게 문의하세요.")
    ;

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}