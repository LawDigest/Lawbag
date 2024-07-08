package com.everyones.lawmaking.global.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuthenticationUtil {

    public static Optional<Long> getUserId() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername().equals("anonymous")) {
            return Optional.empty();
        }
            return Optional.of(Long.parseLong(userDetails.getUsername()));
    }
}
