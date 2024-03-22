package com.everyones.lawmaking.global.jwt;


import com.everyones.lawmaking.global.config.AppProperties;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenProvider {

    private static final String LOCAL_USER_ID = "userId";
    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    private final AppProperties appProperties;

    @Autowired
    public AuthTokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.key = Keys.hmacShaKeyFor(this.appProperties.getAuth().getTokenSecret().getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role,Long userId, Date expiry) {
        return new AuthToken(id, role,userId, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {

        if (authToken.validate()) {

            var claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            UserDetails principal = new User(claims.getSubject(), claims.get(LOCAL_USER_ID).toString(), authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        // 토큰이 검증이 되지 않았을 경우는 게스트를 가진 권한을 부여하게 된다.
        } else {
            var guestAuthority = new SimpleGrantedAuthority("ROLE_GUEST");
            Collection<? extends GrantedAuthority> authorities = Collections.singleton(guestAuthority);
            UserDetails principal = new User("anonymous", "null", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        }
    }

}

