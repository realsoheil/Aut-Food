package com.autfood.server.handlers;

import com.autfood.server.controller.RestaurantService;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;

/** GET /api/restaurants  (لیست همهٔ رستوران‌های APPROVED) */
public class RestaurantListHandler implements HttpHandler {

    private final RestaurantService svc = new RestaurantService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        try {
            byte[] resp = JsonUtil.toJsonBytes(svc.listApproved());
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(200, resp.length);
            ex.getResponseBody().write(resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}
