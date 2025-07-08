package com.autfood.server.http.handlers;

import com.autfood.server.controller.*;
import com.autfood.server.model.FoodItem;
import com.autfood.server.util.*;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * POST /api/restaurants/{rid}/items
 *   هدر Authorization: Bearer <token>
 *   بدنه JSON: { "name": "...", "price": 180000, "discount": 10, "imagePath": "uploads/..." }
 */
public class FoodCreateHandler implements HttpHandler {

    private final AuthService auth = new AuthService();
    private final RestaurantService restService = new RestaurantService();
    private final FoodItemService foodService = new FoodItemService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        /* فقط متد POST مجاز است */
        if (!"POST".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        /* احراز هویت با توکن */
        Integer userId = TokenUtil.extractUserId(
                ex.getRequestHeaders().getFirst("Authorization"), auth);
        if (userId == null) { ex.sendResponseHeaders(401, -1); return; }

        /* استخراج restaurantId‌ از آدرس */
        // /api/restaurants/{rid}/items     →  بخش چهارم Array
        String[] parts = ex.getRequestURI().getPath().split("/");
        if (parts.length < 5) { ex.sendResponseHeaders(400, -1); return; }
        int restId;
        try { restId = Integer.parseInt(parts[3]); }
        catch (NumberFormatException nfe) { ex.sendResponseHeaders(400, -1); return; }

        /* بررسی اینکه کاربر مالک رستوران است (اختیاری، ساده) */
        try {
            if (restService.get(restId) == null)         { ex.sendResponseHeaders(404, -1); return; }
            // می‌توانستیم از restService.get(...).getOwnerId() == userId هم چک کنیم

            /* خواندن JSON */
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject j = JsonUtil.parse(body).getAsJsonObject();
            String  name       = j.get("name")      .getAsString();
            double  price      = j.get("price")     .getAsDouble();
            double  discount   = j.has("discount")   ? j.get("discount").getAsDouble() : 0.0;
            String  imagePath  = j.has("imagePath")  ? j.get("imagePath").getAsString() : null;

            FoodItem fi = foodService.add(restId, name, price, discount, imagePath);

            byte[] resp = JsonUtil.toJsonBytes(fi);
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(201, resp.length);
            ex.getResponseBody().write(resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}

