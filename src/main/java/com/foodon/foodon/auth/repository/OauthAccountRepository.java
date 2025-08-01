package com.foodon.foodon.auth.repository;

import com.foodon.foodon.auth.domain.OauthAccount;
import com.foodon.foodon.auth.domain.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthAccountRepository extends JpaRepository<OauthAccount, Long> {

    Optional<OauthAccount> findByProviderIdAndProvider(String providerId, OauthProvider provider);
}
