package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.domain.entity.Provider;
import com.everyones.lawmaking.domain.entity.Role;
import com.everyones.lawmaking.global.auth.socialInfo.OAuth2UserInfo;
import com.everyones.lawmaking.global.auth.socialInfo.OAuth2UserInfoFactory;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.CookieUtil;
import com.everyones.lawmaking.repository.AuthInfoRepository;
import com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.everyones.lawmaking.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final AuthInfoRepository authInfoRepository;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("비인가 Redirect URI를 받아 인증을 진행할 수 없습니다");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        var authToken = (OAuth2AuthenticationToken) authentication;
        Provider provider = Provider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        var user = (OAuth2User) authentication.getPrincipal();
        var userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OAuth2User) authentication.getPrincipal()).getAuthorities();

        var refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        var cookieMaxAge = (int) refreshTokenExpiry *1000*60;
        Map<String, AuthToken> userToken = userTokenToCookie(userInfo, authorities,provider);

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, userToken.get("refreshToken").getToken(), cookieMaxAge);
        CookieUtil.addCookie(response, ACCESS_TOKEN, userToken.get("accessToken").getToken(), cookieMaxAge);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    protected  Map<String,AuthToken> userTokenToCookie(OAuth2UserInfo userInfo,Collection<? extends GrantedAuthority> authorities,Provider provider) {

        Map<String,AuthToken> authTokenMap = new HashMap<String,AuthToken>();
        // access 토큰 설정
        var now = new Date();
        var role = hasAuthority(authorities, Role.ADMIN.getCode()) ? Role.ADMIN : Role.MEMBER;
        var savedUser = userRepository.findBySocialIdAndProvider(userInfo.getId(), provider)
        .orElseThrow();
        var accessTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry()*1000*60;


        AuthToken accessToken = tokenProvider.createAuthToken(
                userInfo.getId(),
                role.getCode(),
                savedUser.getId(),
                new Date(now.getTime() + accessTokenExpiry)
        );

        // refresh 토큰 설정
        var refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry()*1000*60;

        AuthToken refreshToken = tokenProvider.createAuthToken(
                userInfo.getId(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        authTokenMap.put("accessToken", accessToken);
        authTokenMap.put("refreshToken", refreshToken);


        return authTokenMap;

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
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

    private boolean isAuthorizedRedirectUri(String uri) {
        var checkUri = URI.create(uri);

        var clientUri = appProperties.getAuth().getClientRedirectUri();


        return Stream.concat(appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream(), Stream.of(clientUri))
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(clientUri);
                    return authorizedURI.getHost().equals(checkUri.getHost())
                            && authorizedURI.getPort() == checkUri.getPort();
                });
    }
}