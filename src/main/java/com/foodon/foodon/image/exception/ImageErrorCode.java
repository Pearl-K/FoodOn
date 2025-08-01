package com.foodon.foodon.image.exception;

import lombok.Getter;

@Getter
public enum ImageErrorCode {

    ILLEGAL_IMAGE_EXTENSION("10000", "지원하지 않는 이미지 파일 확장자입니다."),
    ILLEGAL_IMAGE_FORMAT("10001", "유효한 이미지 파일이 아닙니다."),
    IMAGE_IS_NULL("10002", "이미지가 존재하지 않습니다.")
    ;

    private final String code;
    private final String message;

    ImageErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
