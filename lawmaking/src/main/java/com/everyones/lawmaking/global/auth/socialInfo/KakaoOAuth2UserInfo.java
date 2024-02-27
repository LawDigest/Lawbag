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
        var id = (String) attributes.get("id");
        log.info(id);
        return id;
    }

    @Override
    public String getName() {
        var name = (String) attributes.get("name");
        log.info(name);

        return name;
    }

    @Override
    public String getEmail() {
        var email = (String) attributes.get("email");
        log.info(email);

        return email;
    }

}
