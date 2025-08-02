package com.everyones.lawmaking.global.config.OAuthConfig;

import java.net.URI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public HttpEntity<MultiValueMap<String, String>> getOAuthTokenRequestEntity(String refreshToken) {
        // 필요한 form 데이터 주입
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("grant_type", "refresh_token");
        requestData.add("client_id", clientId);
        requestData.add("refresh_token", refreshToken);
        requestData.add("client_secret", clientSecret);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HttpEntity 생성
        return new HttpEntity<>(requestData, headers);
    }

    @Override
    public HttpEntity<MultiValueMap<String, String>> getUnlinkRequestEntity(String targetId) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("target_id_type", "user_id");
        requestData.add("target_id", targetId);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + appAdminKey);
        return new HttpEntity<>(requestData, headers);
    }

}
