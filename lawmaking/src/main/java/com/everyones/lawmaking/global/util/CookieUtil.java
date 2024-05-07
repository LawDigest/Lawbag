package com.everyones.lawmaking.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtil {
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = ResponseCookie.from(name,value);
        cookie.path("/");
        cookie.httpOnly(true);
        cookie.secure(true);
        cookie.maxAge(maxAge);
        cookie.sameSite("None");
        response.addHeader("Set-Cookie",cookie.toString());
    }

    public static void addCookieForClient(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = ResponseCookie.from(name, value);
        cookie.path("/");
        cookie.secure(true);
        cookie.maxAge(maxAge);
        cookie.sameSite("None");
        response.addHeader("Set-Cookie",cookie.toString());
    }



    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    var newCookie = ResponseCookie.from(name, null);
                    newCookie.path("/");
                    newCookie.value("");
                    newCookie.httpOnly(true);
                    newCookie.secure(true);
                    newCookie.maxAge(0);
                    newCookie.sameSite("None");
                    response.addHeader("Set-Cookie",newCookie.toString());

                }
            }
        }
    }

    public static void deleteCookieForClient(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    var newCookie = ResponseCookie.from(name, null);
                    newCookie.path("/");
                    newCookie.value("");
                    newCookie.secure(true);
                    newCookie.maxAge(0);
                    newCookie.sameSite("None");
                    response.addHeader("Set-Cookie",newCookie.toString());

                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
