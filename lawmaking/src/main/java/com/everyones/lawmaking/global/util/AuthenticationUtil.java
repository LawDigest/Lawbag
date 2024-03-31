package com.everyones.lawmaking.global.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuthenticationUtil {

    public static Optional<Long> getUserId() {
        Optional<Long> userId;
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername().equals("anonymous")) {
            userId = Optional.empty();
        } else {
            userId = Optional.of(Long.parseLong(userDetails.getUsername()));
        }
        return userId;
    }
}
