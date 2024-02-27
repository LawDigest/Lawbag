package com.everyones.lawmaking.global.auth;

import com.everyones.lawmaking.domain.entity.AuthInfo;
import com.everyones.lawmaking.domain.entity.Provider;
import com.everyones.lawmaking.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PrincipalDetails implements OAuth2User {
    private final String socialId;
    private final Provider providerType;
    private final Role role;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return socialId;
    }


    public static PrincipalDetails create(AuthInfo authInfo, Map<String, Object> attributes) {
        PrincipalDetails userPrincipal = new PrincipalDetails(
                authInfo.getSocialId(),
                authInfo.getProvider(),
                Role.MEMBER,
                Collections.singletonList(new SimpleGrantedAuthority(Role.MEMBER.getCode()))
        );
        // social 정보들을 담는 attributes
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}
