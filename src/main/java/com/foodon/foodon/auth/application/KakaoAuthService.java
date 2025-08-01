package com.foodon.foodon.auth.application;

import com.foodon.foodon.auth.domain.OauthAccount;
import com.foodon.foodon.auth.domain.OauthProvider;
import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.domain.RefreshToken;
import com.foodon.foodon.auth.dto.response.AuthTokenResponse;
import com.foodon.foodon.auth.dto.response.KakaoUserInfoResponse;
import com.foodon.foodon.auth.exception.AuthErrorCode;
import com.foodon.foodon.auth.infrastructure.KakaoApiClient;
import com.foodon.foodon.auth.repository.OauthAccountRepository;
import com.foodon.foodon.auth.repository.RefreshTokenRepository;
import com.foodon.foodon.auth.util.JwtUtil;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.foodon.foodon.auth.exception.AuthException.AuthUnauthorizedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final OauthAccountRepository oauthAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthTokenResponse loginByKakao(String kakaoAccessToken) {
        KakaoUserInfoResponse kakaoUser = kakaoApiClient.getUserInfo(kakaoAccessToken);
        Member member = findOrCreateMember(kakaoUser);
        MemberTokens tokens = issueTokensFor(member);

        return buildLoginResponse(tokens, member.isProfileUpdated());
    }

    private Member findOrCreateMember(KakaoUserInfoResponse kakaoUser) {
        return oauthAccountRepository
                .findByProviderIdAndProvider(kakaoUser.id().toString(), OauthProvider.KAKAO)
                .map(account -> memberRepository.findById(account.getMemberId())
                        .orElseThrow(() -> new AuthUnauthorizedException(AuthErrorCode.CORRUPTED_OAUTH_LINK)))
                .orElseGet(() -> createMemberAndLinkOauth(kakaoUser));
    }

    private Member createMemberAndLinkOauth(KakaoUserInfoResponse kakaoUser) {
        Member newMember = memberRepository.save(
                Member.createMember(
                        kakaoUser.kakaoAccount().profile().nickname(),
                        kakaoUser.kakaoAccount().email(),
                        kakaoUser.kakaoAccount().profile().profileImageUrl()
                )
        );

        OauthAccount oauthAccount = OauthAccount.of(
                kakaoUser.id().toString(),
                OauthProvider.KAKAO,
                newMember.getId()
        );
        oauthAccountRepository.save(oauthAccount);

        return newMember;
    }

    private MemberTokens issueTokensFor(Member member) {
        MemberTokens tokens = jwtUtil.createMemberToken(Long.toString(member.getId()));
        refreshTokenRepository.save(new RefreshToken(member.getId(), tokens.refreshToken()));
        return tokens;
    }

    private AuthTokenResponse buildLoginResponse(MemberTokens tokens, boolean profileUpdated) {
        return AuthTokenResponse.of(
                tokens.accessToken(),
                tokens.refreshToken(),
                profileUpdated
        );
    }
}

