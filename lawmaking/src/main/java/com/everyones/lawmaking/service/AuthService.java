package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.WithdrawResponse;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.config.RestTemplateConfig;
import com.everyones.lawmaking.global.error.AuthException;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.HeaderUtil;
import com.everyones.lawmaking.global.util.TokenUtil;
import com.everyones.lawmaking.repository.AuthInfoRepository;
import com.everyones.lawmaking.repository.UserRepository;
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


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final AuthInfoRepository authInfoRepository;
    private final RestTemplateConfig restTemplateUtil;
    private final AppProperties appProperties;
    private final AuthTokenProvider authTokenProvider;
    private final UserRepository userRepository;
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


        // 로그아웃
        String[] cookieNames = {"refreshToken", "accessToken"};

        for (String cookieName : cookieNames) {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }

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

        String accessTokenFromHeader = HeaderUtil.getAccessToken(httpServletRequest);

        //토큰 재발급
        var reissueTokenArgsDto = TokenUtil.ReissueTokenArgsDto.builder()
                .auth(appProperties.getAuth())
                .tokenProvider(authTokenProvider)
                .userRepository(userRepository)
                .accessToken(accessTokenFromHeader)
                .refreshTokenFromCookie(refreshTokenCookie)
                .build();
        var tokenMap = TokenUtil.reissueToken(reissueTokenArgsDto);

        // 쿠키 설정
        String[] cookieNames = {"refreshToken", "accessToken"};

        for (String cookieName : cookieNames) {
            Cookie cookie = new Cookie(cookieName, tokenMap.get(cookieName).getToken());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }

//        return EmptyResponse.instance;
    }


    }
