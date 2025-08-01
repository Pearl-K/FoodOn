package com.foodon.foodon.auth.dto.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        boolean profileUpdated
) {
    public static AuthTokenResponse of(
            String accessToken,
            String refreshToken,
            boolean profileUpdated
    ) {
        return new AuthTokenResponse(
                accessToken,
                refreshToken,
                profileUpdated);
    }
}
