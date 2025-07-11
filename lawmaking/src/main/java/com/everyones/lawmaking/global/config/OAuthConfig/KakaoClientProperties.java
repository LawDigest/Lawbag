package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Component
public class KakaoClientProperties implements OAuth2ClientProperties{
    @Value("${app.auth.kakao-app-client-id}")
    private String clientId;
    @Value("${app.auth.kakao-app-client-secret}")
    private String clientSecret;
    @Value("${app.auth.kakao-withdraw-uri}")
    private String withdrawUri;
    @Value("${app.auth.kakao-app-admin-key}")
    private String appAdminKey;

    @Override
    public URI getOAuthTokenUri() {
        return UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();
    }

    @Override
    public URI getUnlinkUri() {
        return UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v1/user/unlink")
                .encode()
                .build()
                .toUri();
    }

    @Override
    public HttpEntity<Map<String, String>> getOAuthTokenRequestEntity() {
        // 필요한 form 데이터 주입
        Map<String, String> requestData = new ConcurrentHashMap<>();
        requestData.put("grant_type", "refresh_token");
        requestData.put("client_id", clientId);
        requestData.put("client_secret", clientSecret);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HttpEntity 생성
        return new HttpEntity<>(requestData, headers);
    }

    @Override
    public HttpEntity<Map<String, String>> getUnlinkRequestEntity(Object target_id) {
        Map<String, String> requestData = new ConcurrentHashMap<>();
        requestData.put("target_id_type", "user_id");
        requestData.put("target_id", (String) target_id);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(requestData, headers);
    }

}
