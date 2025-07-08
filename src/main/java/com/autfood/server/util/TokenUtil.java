package com.autfood.server.util;

import com.autfood.server.controller.AuthService;

public final class TokenUtil {
    public static Integer extractUserId(String authHeader, AuthService auth){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return auth.validate(token);
    }
    private TokenUtil() {}
}
