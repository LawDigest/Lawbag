package com.everyones.lawmaking.global.service;

import com.everyones.lawmaking.domain.entity.Token;
import com.everyones.lawmaking.global.auth.PrincipalDetails;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.error.AuthException;
import com.everyones.lawmaking.global.error.UserException;
import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.CookieUtil;
import com.everyones.lawmaking.repository.TokenRepository;
import com.everyones.lawmaking.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final AuthTokenProvider tokenProvider;

    private final HttpServletRequest httpServletRequest;

    private final AppProperties appProperties;


    private static final String PROVIDER = "provider";


    public Map<String, String> issueToken(Authentication authentication ) {


        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        var userLocalId = principalDetails.getUserId();
        var userSocialId = principalDetails.getSocialId();
        var userRole = principalDetails.getRole();
        var userProvider = principalDetails.getProviderType();


        Map<String, String> authTokenMap = new HashMap<>();
        // access 토큰 설정
        var now = new Date();

        long minutes = 1000 * 60;

        AuthToken accessToken = tokenProvider.createAuthToken(
                userLocalId,
                userRole.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpiry() * minutes)
        );

        // refresh 토큰 설정

        AuthToken refreshToken = tokenProvider.createAuthToken(
                new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry() * minutes)
        );

        var accessTokenForUse = accessToken.getToken();
        var refreshTokenForUse = refreshToken.getToken();

        var savedUser = userRepository.findUserBySocialIdAndProvider(userSocialId, userProvider)
                .orElseThrow(() -> new UserException.UserNotFoundException(Map.of("userProvider", userProvider.toString())));

        var tokenData = Token.builder()
                .user(savedUser)
                .role(savedUser.getRole())
                .accessToken(accessTokenForUse)
                .refreshToken(refreshTokenForUse)
                .build();

        tokenRepository.save(tokenData);

        authTokenMap.put("accessToken", accessTokenForUse);
        authTokenMap.put("refreshToken", refreshTokenForUse);

        return authTokenMap;
    }

    public Map<String, String> reissueToken(String accessToken, String refreshTokenFromCookie ) {

        // accessToken으로 token 행 가져오기
        var savedToken = tokenRepository.findTokenByAccessToken(accessToken)
                .orElseThrow(AuthException.TokenNotFound::new);

        var refreshTokenForUse = savedToken.getRefreshToken();

        // 가져온 refreshToken 검증
        var refreshTokenForAuthToken = tokenProvider.convertAuthToken(refreshTokenForUse);

        if (!refreshTokenForAuthToken.validate()) {
            throw new AuthException.TokenNotValid();
        }

        // refreshToken 매칭검사
        if (!refreshTokenForUse.equals(refreshTokenFromCookie)) {
            throw new AuthException.TokenNotValid();
        }


        Map<String, String> authTokenMap = new HashMap<>();
        // access 토큰 설정
        var now = new Date();
        var role = savedToken.getRole();

        long minutes = 1000 * 60;

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                savedToken.getUser().getId(),
                role.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpiry() * minutes)
        );

        // refresh 토큰 설정

        AuthToken newRefreshToken = tokenProvider.createAuthToken(
                new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry() * minutes)
        );

        var newAccessTokenForUse = newAccessToken.getToken();
        var newRefreshTokenForUse = newRefreshToken.getToken();


        var tokenData = Token.builder()
                .user(savedToken.getUser())
                .role(savedToken.getUser().getRole())
                .accessToken(newAccessToken.getToken())
                .refreshToken(newRefreshToken.getToken())
                .build();

        tokenRepository.findTokenByRefreshToken(refreshTokenForUse)
                .ifPresent(tokenRepository::delete);

        tokenRepository.save(tokenData);

        authTokenMap.put("accessToken", newAccessTokenForUse);
        authTokenMap.put("refreshToken", newRefreshTokenForUse);


        return authTokenMap;
    }

    public void invalidateToken() {

        //리프레시 토큰으로 사용자 id, provider 가져오기
        var refreshTokenFromCookie = CookieUtil.getCookie(httpServletRequest, REFRESH_TOKEN);
        if (refreshTokenFromCookie.isPresent()) {
            AuthToken refreshToken = tokenProvider.convertAuthToken(refreshTokenFromCookie.get().getValue());
            //사용자 조회해서 refresh 토큰 열 null 처리
            tokenRepository.findTokenByRefreshToken(refreshToken.getToken())
                    .ifPresent(tokenRepository::delete);

        }
    }


    private static boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }


}
