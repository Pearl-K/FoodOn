package com.foodon.foodon.auth.util;

import static com.foodon.foodon.auth.exception.AuthException.AuthUnauthorizedException;

import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.exception.AuthErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpiry;
    private final Long refreshTokenExpiry;
    private final Long superTokenExpiry;

    public JwtUtil(
            @Value("${spring.auth.jwt.secret-key}") final String secretKey,
            @Value("${spring.auth.jwt.access-token-expiry}") final Long accessTokenExpiry,
            @Value("${spring.auth.jwt.refresh-token-expiry}") final Long refreshTokenExpiry,
            @Value("${spring.auth.jwt.super-token-expiry}") final Long superTokenExpiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.superTokenExpiry = superTokenExpiry;
    }

    // ---------- * 토큰 생성 * ---------- //

    public MemberTokens createMemberToken(String subject) {
        String accessToken = createToken(subject, accessTokenExpiry);
        String refreshToken = createToken("", refreshTokenExpiry);
        return new MemberTokens(accessToken, refreshToken);
    }

    public MemberTokens createSuperToken(String subject) {
        String accessToken = createToken(subject, superTokenExpiry);
        String refreshToken = createToken("", superTokenExpiry);
        return new MemberTokens(accessToken, refreshToken);
    }

    private String createToken(String subject, Long expiredMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String reissueAccessToken(String subject) {
        return createToken(subject, accessTokenExpiry);
    }

    // ---------- * 토큰 정보 추출 (Subject) * ---------- //

    public String getSubject(String token) {
        return parseToken(token)
                .getBody().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    // ---------- * 토큰 검증 (keys, expiry) * ---------- //

    public void validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
        } catch (JwtException e) {
            throw new AuthUnauthorizedException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public boolean isAccessTokenExpired(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}