package com.autfood.server.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionStore {
    private static final Map<String,Integer> TOKENS = new ConcurrentHashMap<>();

    public static String createToken(int userId){
        String token = UUID.randomUUID().toString();
        TOKENS.put(token,userId);
        return token;
    }
    public static Integer getUserId(String token){
        return TOKENS.get(token);
    }
    private SessionStore(){}
}
