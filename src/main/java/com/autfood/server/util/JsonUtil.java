package com.autfood.server.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

public final class JsonUtil {
    private static final Gson GSON = new Gson();

    private JsonUtil() {}

    public static byte[] toJsonBytes(Object obj) {
        return GSON.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    // ✅ متد مورد نیاز برای parse کردن رشته JSON
    public static JsonElement parse(String json) {
        return JsonParser.parseString(json);
    }
}
