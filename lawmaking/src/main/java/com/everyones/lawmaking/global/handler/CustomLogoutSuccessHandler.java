package com.everyones.lawmaking.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;
import java.util.List;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    public final static String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    private final List<String> allowedUrls;

    public CustomLogoutSuccessHandler(String defaultTargetUrl, List<String> allowedUrls) {
        super.setDefaultTargetUrl(defaultTargetUrl);
        this.allowedUrls = allowedUrls;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (redirectUrl != null && allowedUrls.contains(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:3000/login");
        }
    }
}