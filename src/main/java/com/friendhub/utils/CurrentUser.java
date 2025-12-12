package com.friendhub.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CurrentUser {

    public static Long id() {
        JwtAuthenticationToken auth =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ((Number) auth.getTokenAttributes().get("user_id")).longValue();
    }

    public static String email() {
        JwtAuthenticationToken auth =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (String) auth.getTokenAttributes().get("email");
    }

    public static String role() {
        JwtAuthenticationToken auth =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (String) auth.getTokenAttributes().get("scope");
    }

}
