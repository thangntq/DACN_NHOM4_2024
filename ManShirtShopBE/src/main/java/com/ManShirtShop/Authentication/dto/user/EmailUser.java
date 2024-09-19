package com.ManShirtShop.Authentication.dto.user;

import org.springframework.security.core.context.SecurityContextHolder;

public class EmailUser {
    private EmailUser() {

    }

    public static String getEmailUser() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return principal;
    }
}
