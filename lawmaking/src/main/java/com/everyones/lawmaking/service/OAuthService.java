package com.everyones.lawmaking.service;


import com.everyones.lawmaking.common.dto.response.KakaoAccessTokenRefreshResponse;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.error.SocialTokenException;
import com.everyones.lawmaking.repository.OAuth2ClientTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {

    private final AppProperties appProperties;
    private final OAuth2ClientTokenRepository oAuth2ClientTokenRepository;
    private static final String PRINCIPAL_NAME_STRING = "socialTokenPrincipalName";

    private final RestTemplate restTemplate;



    //소셜 엑세스토큰 재발급 로직
    // 데이터베이스에 저장된 소셜 리프레시토큰을 활용해 엑세스토큰 발급
    public ResponseEntity<KakaoAccessTokenRefreshResponse> refreshAccessToken(String clientRegistrationId, String socialId){
        var auth = appProperties.getAuth();
        var refreshToken = oAuth2ClientTokenRepository.findTop1ByClientRegistrationIdAndPrincipalNameOrderByCreatedDateDesc(clientRegistrationId, socialId)
                .orElseThrow(() -> new SocialTokenException.SocialTokenNotFound(Map.of(PRINCIPAL_NAME_STRING, socialId)))
                .getRefreshToken();
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();
        // MultiValueMap으로 요청 데이터 설정
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("grant_type", "refresh_token");
        requestData.add("client_id", auth.getKakaoAppClientId());
        requestData.add("client_secret", auth.getKakaoAppClientSecret());
        requestData.add("refresh_token", refreshToken);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestData, headers);
        var kakaoAccessTokenRefreshResponse = restTemplate.postForEntity(uri, requestEntity, KakaoAccessTokenRefreshResponse.class);
        return ResponseEntity.ok(kakaoAccessTokenRefreshResponse.getBody());

    }

    //소셜 연결 끊기
    public String unlink(String socialId, String accessToken){
        // MultiValueMap으로 요청 데이터 설정
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("target_id_type", "user_id");
        requestData.add("target_id", socialId);

        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v1/user/unlink")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestData, headers);
            return restTemplate.postForEntity(uri, requestEntity, String.class).getBody();

    }

}
