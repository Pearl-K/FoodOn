package com.foodon.foodon.auth.dto;

public record MemberTokens(
        String accessToken,
        String refreshToken
) {

}