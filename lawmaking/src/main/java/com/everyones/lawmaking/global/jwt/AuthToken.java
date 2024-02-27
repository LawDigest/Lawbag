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

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    AuthToken(String id, String email, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, email, expiry);
    }
    // access Token 발급
    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    // refresh Token 발급
    private String createAuthToken(String id, String email, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(EMAIL_KEY, email)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }



    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    // ToDo(Exception 처리해야함)
    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

}
