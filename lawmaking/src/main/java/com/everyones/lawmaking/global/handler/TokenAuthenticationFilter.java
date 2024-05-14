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
            //시큐리티컨텍스트홀더 초기화
            SecurityContextHolder.clearContext();

            String tokenStr = HeaderUtil.getAccessToken(request);
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);

            var authentication = tokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);



            // 토큰이 없을 경우 게스트사용자 권한을 SecurityContext에 부여해주고


        }
        catch (ExpiredJwtException eje){
            eje.printStackTrace();
            request.setAttribute("expiredJwtException", eje);
        }
        catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            e.printStackTrace();
            request.setAttribute("SecurityExceptionForJwt", e);
        }
        catch(IllegalArgumentException e){
            request.setAttribute("IllegalArgumentException", e);
            e.printStackTrace();

        }
        catch(Exception e){
            request.setAttribute("Exception", e);
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);

    }
}
