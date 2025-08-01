package com.foodon.foodon.auth.resolver;

import static com.foodon.foodon.auth.exception.AuthException.AuthUnauthorizedException;
import static com.foodon.foodon.member.exception.MemberException.MemberNotFoundException;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.auth.exception.AuthErrorCode;
import com.foodon.foodon.auth.util.JwtUtil;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Member resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new AuthUnauthorizedException(AuthErrorCode.UNAUTHORIZED_ACCESS);
        }

        //String refreshToken = extractRefreshToken(request);
        String accessToken = extractAccessToken(request);

        if (jwtUtil.isAccessTokenValid(accessToken)) {
            return extractMember(accessToken);
        }

        throw new AuthUnauthorizedException(AuthErrorCode.FAILED_TO_VALIDATE_TOKEN);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new AuthUnauthorizedException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        return authHeader.substring("Bearer ".length());
    }

    private String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("X-Refresh-Token");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthUnauthorizedException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        return refreshToken;
    }

    private Member extractMember(String accessToken) {
        Long userId = Long.valueOf(jwtUtil.getSubject(accessToken));

        return memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
