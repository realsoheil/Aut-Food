package com.autfood.server.util;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;

public final class JsonUtil {
    private static final Gson GSON = new Gson();
    private JsonUtil() {}
    public static byte[] toJsonBytes(Object obj) {
        return GSON.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }
}

