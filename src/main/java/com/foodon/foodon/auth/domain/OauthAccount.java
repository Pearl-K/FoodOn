package com.foodon.foodon.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oauth_account", indexes = {
        @Index(name = "idx_provider_id_provider", columnList = "providerId, provider")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private OauthProvider provider;

    private Long memberId;

    public static OauthAccount of(
            String providerId,
            OauthProvider provider,
            Long memberId
    ) {
        OauthAccount account = new OauthAccount();
        account.providerId = providerId;
        account.provider = provider;
        account.memberId = memberId;
        return account;
    }
}
