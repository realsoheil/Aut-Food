package com.autfood.server.handlers;

import com.autfood.server.controller.AuthService;
import com.autfood.server.controller.RestaurantService;
import com.autfood.server.model.Restaurant;
import com.autfood.server.util.JsonUtil;
import com.autfood.server.util.TokenUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** POST /api/restaurants  (فروشنده رستوران جدید ثبت می‌کند) */
public class RestaurantCreateHandler implements HttpHandler {

    private final AuthService auth = new AuthService();
    private final RestaurantService svc = new RestaurantService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        // 1) احراز هویت
        Integer ownerId = TokenUtil.extractUserId(ex.getRequestHeaders().getFirst("Authorization"), auth);
        if (ownerId == null) { ex.sendResponseHeaders(401, -1); return; }

        // 2) خواندن JSON
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject j = JsonUtil.parse(body).getAsJsonObject();
        String name = j.get("name").getAsString();
        String desc = j.has("description") ? j.get("description").getAsString() : null;

        try {
            Restaurant r = svc.create(ownerId, name, desc);           // status = PENDING
            byte[] resp = JsonUtil.toJsonBytes(r);
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(201, resp.length);
            ex.getResponseBody().write(resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}


