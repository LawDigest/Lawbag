package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleClientProperties implements OAuth2ClientProperties{
    @Value("${app.auth.google-app-client-id}")
    private String clientId;
    @Value("${app.auth.google-app-client-secret}")
    private String clientSecret;
    @Value("${app.auth.google-withdraw-uri}")
    private String withdrawUri;

    @Override
    public URI getOAuthTokenUri() {
        return null;
    }

    @Override
    public URI getUnlinkUri() {
        return null;
    }

    @Override
    public HttpEntity getOAuthTokenRequestEntity(String refreshToken) {
        return null;
    }

    @Override
    public HttpEntity getUnlinkRequestEntity(String socialId) {
        return null;
    }
}