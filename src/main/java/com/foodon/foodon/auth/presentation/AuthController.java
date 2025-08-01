package com.foodon.foodon.auth.presentation;

import com.foodon.foodon.auth.application.AuthService;
import com.foodon.foodon.auth.application.KakaoAuthService;
import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.dto.request.KakaoLoginRequest;
import com.foodon.foodon.auth.dto.response.AuthTokenResponse;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final AuthService authService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인 후 서버 자체 인증 토큰 발급")
    public ResponseEntity<Response<AuthTokenResponse>> loginByKakao(
            @RequestBody KakaoLoginRequest request
    ) {
        AuthTokenResponse response = kakaoAuthService.loginByKakao(request.accessToken());
        return ResponseUtil.success(response);
    }

    @PostMapping("/super-token")
    @Operation(summary = "API 테스트를 위한 기한 30일 임시 슈퍼 토큰 발급")
    public ResponseEntity<Response<MemberTokens>> createSuperToken(
            @RequestParam String userId
    ) {
        MemberTokens superTokens = authService.createSuperToken(userId);
        return ResponseUtil.success(superTokens);
    }

    @PostMapping("/token/validate")
    @Operation(summary = "자동 로그인을 위한 토큰 검증")
    public ResponseEntity<Response<AuthTokenResponse>> validateTokens(
            @RequestBody MemberTokens memberTokens
    ) {
        AuthTokenResponse response = authService.validateAndReissueTokens(memberTokens);
        return ResponseUtil.success(response);
    }

}
