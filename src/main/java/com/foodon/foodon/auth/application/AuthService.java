package com.foodon.foodon.auth.application;

import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.dto.response.AuthTokenResponse;
import com.foodon.foodon.auth.util.JwtUtil;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.foodon.foodon.member.exception.MemberException.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public MemberTokens createSuperToken(String userId) {
        validateMemberExists(userId);
        return jwtUtil.createSuperToken(userId);
    }

    public AuthTokenResponse validateAndReissueTokens(MemberTokens memberTokens) {
        jwtUtil.validateRefreshToken(memberTokens.refreshToken());
        String subject = extractSubjectAllowingExpiration(memberTokens.accessToken());
        Member member = findMemberOrThrow(subject);
        MemberTokens tokens = jwtUtil.createMemberToken(subject);

        return AuthTokenResponse.of(
                tokens.accessToken(),
                tokens.refreshToken(),
                member.isProfileUpdated()
        );
    }

    private Member findMemberOrThrow(String userId) {
        return memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private String extractSubjectAllowingExpiration(String accessToken) {
        try {
            return jwtUtil.getSubject(accessToken);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    private void validateMemberExists(String userId) {
        if (!memberRepository.existsById(Long.valueOf(userId))) {
            throw new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
