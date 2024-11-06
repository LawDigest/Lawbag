package com.everyones.lawmaking.global.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Auth auth;
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        @Value("${app.auth.token-secret}")
        private  String tokenSecret;

        @Value("${app.auth.kakao-app-client-id}")
        private  String kakaoAppClientId;

        @Value("${app.auth.kakao-app-client-secret}")
        private String kakaoAppClientSecret;

        @Value("${app.auth.access-token-expiry}")
        private  long accessTokenExpiry;

        @Value("${app.auth.refresh-token-expiry}")
        private  long refreshTokenExpiry;


        @Value("${app.auth.kakao-app-admin-key}")
        private String kakaoAppAdminKey;

        @Value("${app.auth.kakao-withdraw-uri}")
        private String kakaoWithdrawUri;

        @Value("${app.auth.cookie-domain}")
        private String cookieDomain;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static final class OAuth2 {

        @Value("${app.oauth2.client-redirect-uri}")
        private List<String> clientRedirectUri;

    }
}
