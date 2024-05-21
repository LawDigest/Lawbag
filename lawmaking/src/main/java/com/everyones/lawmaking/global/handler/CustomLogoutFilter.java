package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.service.TokenService;
import com.everyones.lawmaking.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        if (isLogoutRequest(request)) {
            handleLogout(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        // 로그아웃 요청 판별 로직
        return request.getMethod().equals("POST") && request.getRequestURI().equals("/v1/logout");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            session.invalidate();
        }


        var cookieDomain = appProperties.getAuth().getCookieDomain();
        //refresh토큰 비활성화
        // 로그아웃 되면 토큰 재발급을 방지함.
        tokenService.invalidateToken();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.deleteCookie(request, response, JSESSIONID);
        CookieUtil.deleteCookieForClient(request,response,ACCESS_TOKEN,cookieDomain);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
