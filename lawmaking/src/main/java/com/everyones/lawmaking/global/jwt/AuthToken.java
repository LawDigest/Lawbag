package com.everyones.lawmaking.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AuthToken {

    private final String token;
    private final Key key;

    private static final String EMAIL_KEY = "email";

    private static final String AUTHENTICATION_KEY = "role";

    private static final String LOCAL_USER_ID = "userId";



    AuthToken(String id,String role,Long userId, Date expiry, Key key) {
        this.key = key;
        this.token = createAccessToken(id,role,userId, expiry);
    }

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createRefreshToken(id, expiry);
    }
    // access Token 발급
    private String createAccessToken(String id,String role,Long userId, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHENTICATION_KEY,role)
                .claim(LOCAL_USER_ID,userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    // refresh Token 발급
    private String createRefreshToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }



    public boolean validate()  {
        return this.getTokenClaims() != null;
    }

    // ToDo(Exception 처리해야함)
    public Claims getTokenClaims()  {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            return null;
        }
    }

}
