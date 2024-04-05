package com.everyones.lawmaking.global.handler;

import com.everyones.lawmaking.global.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    public final static String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";




    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResponse.ok("logout successed");
    }
}