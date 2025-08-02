package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import org.springframework.http.HttpEntity;

public interface OAuth2ClientProperties {

    URI getOAuthTokenUri();

    URI getUnlinkUri();

    HttpEntity getOAuthTokenRequestEntity(String refreshToken);

    HttpEntity getUnlinkRequestEntity(String socialId);

}
