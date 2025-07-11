package com.everyones.lawmaking.global.config.OAuthConfig;

import com.everyones.lawmaking.domain.entity.Provider;

public class OAuthPropertiesFactory {

    public static OAuth2ClientProperties getClientProperties(Provider provider) {
        if (provider.equals(Provider.NAVER)) {
            return new NaverClientProperties();
        } else if(provider.equals(Provider.KAKAO)) {
            return new KakaoClientProperties();
        } else {
            return new GoogleClientProperties();
        }
    }
}
