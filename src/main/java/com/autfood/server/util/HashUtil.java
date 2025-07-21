package com.autfood.server.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class HashUtil {
    private static final SecureRandom RAND = new SecureRandom();

    public static String randomSalt() {
        byte[] b = new byte[16];
        RAND.nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private HashUtil(){}public static String hashPassword(String password, String salt) {
        return sha256(password + salt);
    }


}
