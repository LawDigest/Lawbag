package com.everyones.lawmaking.global.auth.socialInfo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;

    }

    @Override
    public String getId() {
        var id = String.valueOf(attributes.get("id"));
        return id;
    }

    @Override
    public String getName() {
        var properties= (Map<String, Object>)attributes.get("properties");
        var name = String.valueOf(properties.get("nickname"));

        return name;
    }

    @Override
    public String getEmail() {
        var kakaoAccount= (Map<String, Object>)attributes.get("kakao_account");
        var email = String.valueOf(kakaoAccount.get("email"));

        return email;
    }

}
