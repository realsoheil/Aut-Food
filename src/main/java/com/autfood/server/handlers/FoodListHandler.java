package com.autfood.server.handlers;

import com.autfood.server.controller.FoodItemService;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;

/**
 * GET /api/restaurants/{rid}/items
 * خروجی: آرایهٔ JSON از آیتم‌های AVAILABLE آن رستوران
 */
public class FoodListHandler implements HttpHandler {

    private final FoodItemService svc = new FoodItemService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        String[] parts = ex.getRequestURI().getPath().split("/");
        if (parts.length < 5) { ex.sendResponseHeaders(400, -1); return; }
        int restId;
        try { restId = Integer.parseInt(parts[3]); }
        catch (NumberFormatException nfe) { ex.sendResponseHeaders(400, -1); return; }

        try {
            byte[] resp = JsonUtil.toJsonBytes(svc.listByRestaurant(restId));
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(200, resp.length);
            ex.getResponseBody().write(resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}
