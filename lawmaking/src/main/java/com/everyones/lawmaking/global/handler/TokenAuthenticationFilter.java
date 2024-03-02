package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {
        String tokenStr = HeaderUtil.getAccessToken(request);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        // 토큰이 없을 경우 익명사용자 권한을 SecurityContextHolder에 부여해주고
        // 토큰이 있을 경우 검증을 해주고 검증이 되지 않으면 GUEST 권한을 주도록 하자.
        //시큐리티 컨텍스트 초기화
        SecurityContextHolder.clearContext();
        // token내용을 가지고 GUEST인지 MEMBER인지 검증함
        var authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);

    }
}
