package com.everyones.lawmaking.global.config.OAuthConfig;

import com.everyones.lawmaking.domain.entity.Provider;

public class OAuthPropertiesFactory {

    private OAuthPropertiesFactory() {

    }
    public static OAuth2ClientProperties getClientProperties(Provider provider) {
        if (provider.equals(Provider.NAVER)) {
            return new NaverClientProperties();
        } else if(provider.equals(Provider.KAKAO)) {
            return new KakaoClientProperties();
        } else if(provider.equals(Provider.GOOGLE)){
            return new GoogleClientProperties();
        } else {
            throw new IllegalArgumentException("OAuth 활용 시 올바른 Provider가 제공되지 않았습니다.");
        }
    }
}
