package com.foodon.foodon.auth.dto.response;

public record KakaoLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static KakaoLoginResponse of(
            String accessToken,
            String refreshToken
    ) {
        return new KakaoLoginResponse(
                accessToken,
                refreshToken
        );
    }
}
