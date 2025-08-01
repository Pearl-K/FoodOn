package com.foodon.foodon.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
        Long id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount,
        Properties properties
) {
    public record KakaoAccount(
            String email,
            Profile profile
    ) {
        public record Profile(
                String nickname,
                @JsonProperty("profile_image_url")
                String profileImageUrl
        ) {}
    }

    public record Properties(
            String nickname,
            @JsonProperty("profile_image")
            String profileImage
    ) {}
}
