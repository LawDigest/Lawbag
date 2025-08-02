package com.everyones.lawmaking.global.config.OAuthConfig;

import com.everyones.lawmaking.domain.entity.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthPropertiesFactory {

    private final KakaoClientProperties kakaoClientProperties;
    private final NaverClientProperties naverClientProperties;
    private final GoogleClientProperties googleClientProperties;

    public OAuth2ClientProperties getClientProperties(Provider provider) {
        return switch (provider) {
            case NAVER -> naverClientProperties;
            case KAKAO -> kakaoClientProperties;
            case GOOGLE -> googleClientProperties;
            default -> throw new IllegalArgumentException("OAuth 활용 시 올바른 Provider가 제공되지 않았습니다.");
        };
    }
}
