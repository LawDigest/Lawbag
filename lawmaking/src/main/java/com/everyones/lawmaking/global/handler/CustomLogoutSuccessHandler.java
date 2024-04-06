package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.global.util.CookieUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.ACCESS_TOKEN;
import static com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {




    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);

        BaseResponse<String> responseMessage = BaseResponse.ok("Logout succeeded");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseMessage));
        response.getWriter().flush();
        response.setStatus(200);
    }
}