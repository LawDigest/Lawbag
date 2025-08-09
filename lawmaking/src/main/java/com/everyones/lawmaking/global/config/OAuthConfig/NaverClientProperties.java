package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NaverClientProperties implements OAuth2ClientProperties{
    @Value("${app.auth.naver-app-client-id}")
    private String clientId;
    @Value("${app.auth.naver-app-client-secret}")
    private String clientSecret;
    @Value("${app.auth.naver-withdraw-uri}")
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
    public HttpEntity getOAuthTokenRequestEntity() {
        return null;
    }

    @Override
    public HttpEntity getUnlinkRequestEntity(Object targetId) {
        return null;
    }
}
