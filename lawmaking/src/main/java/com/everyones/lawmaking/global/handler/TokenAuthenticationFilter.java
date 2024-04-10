package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.util.HeaderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {
        try{
            String tokenStr = HeaderUtil.getAccessToken(request);
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            //시큐리티컨텍스트홀더 초기화
            SecurityContextHolder.clearContext();

            // 토큰이 없을 경우 게스트사용자 권한을 SecurityContext에 부여해주고
            if( tokenStr == null || tokenStr.isEmpty() || request.getRequestURI().contains("public")){
                setGuestAuthentication();
            }
            // 토큰이 있을 경우 검증을 해주고 검증이 되지 않으면 GUEST 권한을 주도록 하자.
            else{
                // token내용을 가지고 GUEST인지 MEMBER인지 검증함
                var authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        catch (ExpiredJwtException eje){
            request.setAttribute("expiredJwtException", eje);
        }
        catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            request.setAttribute("SecurityException", e);
            setGuestAuthentication();
        }
        catch(IllegalArgumentException e){

        }

        filterChain.doFilter(request, response);

    }

    private void setGuestAuthentication() {
        var guestAuthority = new SimpleGrantedAuthority("ROLE_GUEST");
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(guestAuthority);
        UserDetails principal = new User("anonymous", "", authorities);
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
