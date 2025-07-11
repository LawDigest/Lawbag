package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.OAuthTokenResponse;
import com.everyones.lawmaking.domain.entity.Provider;
import com.everyones.lawmaking.global.config.OAuthConfig.OAuthPropertiesFactory;
import com.everyones.lawmaking.global.error.SocialTokenException;
import com.everyones.lawmaking.repository.OAuth2ClientTokenRepository;
import java.net.URI;
import java.util.Map;
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

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {

    private final OAuth2ClientTokenRepository oAuth2ClientTokenRepository;
    private static final String PRINCIPAL_NAME_STRING = "socialTokenPrincipalName";

    private final RestTemplate restTemplate;


    protected String getRecentSavedRefreshToken(Provider provider, String socialId) {
            return oAuth2ClientTokenRepository.findTop1ByClientRegistrationIdAndPrincipalNameOrderByCreatedDateDesc(provider.name(), socialId)
                .orElseThrow(() -> new SocialTokenException.SocialTokenNotFound(Map.of(PRINCIPAL_NAME_STRING, socialId)))
                .getRefreshToken();
    }
    //소셜 엑세스토큰 재발급 로직
    // 데이터베이스에 저장된 소셜 리프레시토큰을 활용해 엑세스토큰 발급
    public ResponseEntity<OAuthTokenResponse> getOAuthTokenResponse(Provider provider, String socialId){
        var oAuthClientProperties = OAuthPropertiesFactory.getClientProperties(provider);
        var uri = oAuthClientProperties.getOAuthTokenUri();
        var requestEntity = oAuthClientProperties.getOAuthTokenRequestEntity();
        var kakaoAccessTokenRefreshResponse = restTemplate.postForEntity(uri, requestEntity, OAuthTokenResponse.class);
        return ResponseEntity.ok(kakaoAccessTokenRefreshResponse.getBody());

    }

    //소셜 연결 끊기
    public void unlink(Provider provider, String socialId){
        // MultiValueMap으로 요청 데이터 설정
        var oAuthClientProperties = OAuthPropertiesFactory.getClientProperties(provider);
        var uri = oAuthClientProperties.getUnlinkUri();

        var requestEntity = oAuthClientProperties.getUnlinkRequestEntity(socialId);
        restTemplate.postForEntity(uri, requestEntity, String.class).getBody();

    }

}
