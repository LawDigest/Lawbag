package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.WithdrawResponse;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.config.RestTemplateConfig;
import com.everyones.lawmaking.global.error.AuthException;
import com.everyones.lawmaking.global.service.TokenService;
import com.everyones.lawmaking.global.util.CookieUtil;
import com.everyones.lawmaking.repository.AuthInfoRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.Map;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final AuthInfoRepository authInfoRepository;
    private final RestTemplateConfig restTemplateUtil;
    private final AppProperties appProperties;
    private final TokenService tokenService;
    private static final String TARGET_ID_TYPE = "user_id";

    @Transactional
    public WithdrawResponse withdraw(String userId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        var authInfoSaved = authInfoRepository.findAuthInfoByUserId(userId)
                .orElseThrow(() -> new AuthException.AuthInfoNotFound(Map.of("userId", userId)));



        try {
            // 카카오 서버에 회원탈퇴 포스트 날림
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","KakaoAK " + appProperties.getAuth().getKakaoAppAdminKey());

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("target_id_type", TARGET_ID_TYPE);
            params.add("target_id", authInfoSaved.getSocialId());

            HttpEntity<MultiValueMap<String, String>> kakaoWithdrawRequest = new HttpEntity<>(params, headers);
            String url = appProperties.getAuth().getKakaoWithdrawUri();

            restTemplateUtil.restTemplate().postForEntity(url, kakaoWithdrawRequest, String.class);

        } catch (RestClientException e) {
            log.error("카카오 계정 연결 해제 중 오류가 발생하였습니다.", e);
            throw new AuthException.ThirdPartyError();
        }

        // userId기반으로 authInfo 가져와서 일괄 삭제
        authInfoRepository.delete(authInfoSaved);

        var cookieDomain = appProperties.getAuth().getCookieDomain();
        // 로그아웃
        tokenService.invalidateToken();
        CookieUtil.deleteCookieForClient(httpServletRequest,httpServletResponse,ACCESS_TOKEN,cookieDomain);
        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, REFRESH_TOKEN);
        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, JSESSIONID);

        HttpSession session = httpServletRequest.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            session.invalidate();
        }

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();




        return WithdrawResponse.of(authInfoSaved);
    }

    @Transactional
    public void reissueToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // 쿠키에서 Refresh Token 가져오기
        Cookie[] cookies = httpServletRequest.getCookies();

        String refreshTokenCookie = null;
        if (cookies == null) {
            throw new AuthException.CookieNotFound();
        }
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshTokenCookie = cookie.getValue();
                break;
            }
        }

        if (refreshTokenCookie == null) {
            throw new AuthException.CookieNotFound();
        }

        //토큰 재발급
        var tokenMap = tokenService.reissueToken(refreshTokenCookie);

        // 쿠키 설정
        var minutes = 1000 * 60;
        var refreshTokenExpiry = (int) appProperties.getAuth().getRefreshTokenExpiry() * minutes;
        var accessTokenExpiry = (int) appProperties.getAuth().getAccessTokenExpiry() * minutes;
        var cookieDomain = appProperties.getAuth().getCookieDomain();

        CookieUtil.deleteCookieForClient(httpServletRequest,httpServletResponse,ACCESS_TOKEN,cookieDomain);
        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, REFRESH_TOKEN);
        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, JSESSIONID);

        CookieUtil.addCookie(httpServletResponse, REFRESH_TOKEN, tokenMap.get("refreshToken"), refreshTokenExpiry);
        CookieUtil.addCookieForClient(httpServletResponse, ACCESS_TOKEN, tokenMap.get("accessToken"), accessTokenExpiry,cookieDomain);
    }
    }
