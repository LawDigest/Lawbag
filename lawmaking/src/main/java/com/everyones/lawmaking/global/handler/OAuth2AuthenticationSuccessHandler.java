package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.CookieUtil;
import com.everyones.lawmaking.global.util.TokenUtil;
import com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.everyones.lawmaking.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
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


        var minutes = 1000 * 60;

//        var cookieForAccessToken = (int) appProperties.getAuth().getAccessTokenExpiry() * minutes;
        var cookieForRefreshToken = (int) appProperties.getAuth().getRefreshTokenExpiry() * minutes;



        var issueTokenArgsDto = TokenUtil.IssueTokenArgsDto.builder()
                .auth(appProperties.getAuth())
                .userRepository(userRepository)
                .authentication(authentication)
                .tokenProvider(tokenProvider)
                .build();

        Map<String, AuthToken> userToken = TokenUtil.issueToken(issueTokenArgsDto);

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, userToken.get("refreshToken").getToken(), cookieForRefreshToken);
//        CookieUtil.addCookie(response, ACCESS_TOKEN, userToken.get("accessToken").getToken(), cookieMaxAge);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", userToken.get("accessToken").getToken())
                .build().toUriString();
    }



    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
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