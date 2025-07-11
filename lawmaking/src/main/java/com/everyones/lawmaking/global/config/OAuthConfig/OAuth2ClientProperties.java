package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import org.springframework.http.HttpEntity;

public interface OAuth2ClientProperties {

    String getClientId();

    String getClientSecret();

    String getWithdrawUri();

    URI getOAuthTokenUri();

    URI getUnlinkUri();

    HttpEntity getOAuthTokenRequestEntity();

    HttpEntity getUnlinkRequestEntity(Object targetId);

}
