package com.foodon.foodon.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorCode {

    ILLEGAL_NICKNAME_NULL("30000", "닉네임은 필수로 입력해야합니다."),
    ILLEGAL_NICKNAME_LENGTH("30001", "닉네임 길이는 최소 1 자에서 20자여야 합니다."),
    ILLEGAL_EMAIL_NULL("30002", "이메일은 필수로 입력해야합니다."),
    ILLEGAL_EMAIL_PATTERN("30003", "올바르지 않은 이메일 형식입니다."),
    ILLEGAL_REQUEST_PARAMETER("30004", "필수값이 잘못된 요청입니다"),
    FORBIDDEN_MEMBER_STATUS("30005", "회원 정보를 볼 수 없습니다."),
    MEMBER_NOT_FOUND("30006", "존재하지 않는 회원입니다."),
    ILLEGAL_SOCIAL_ACCOUNT_ALREADY_EXISTS("30007", "해당 소셜 계정과 연동된 계정이 이미 존재합니다."),
    MEMBER_STATUS_NOT_FOUND("30008", "회원의 상태 이력이 존재하지 않습니다."),
    ACTIVITY_LEVEL_NOT_FOUND("30009","회원의 활동량이 존재하지 않습니다."),
    NUTRIENT_PLAN_NOT_FOUND("30010", "회원의 관리 유형이 존재하지 않습니다.")
    ;

    private final String code;
    private final String message;

    MemberErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
