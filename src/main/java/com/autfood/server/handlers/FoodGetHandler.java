package com.autfood.server.http.handlers;

import com.autfood.server.controller.FoodItemService;
import com.autfood.server.model.FoodItem;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;

/**
 * GET /api/items/{id}
 */
public class FoodGetHandler implements HttpHandler {

    private final FoodItemService svc = new FoodItemService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        String[] parts = ex.getRequestURI().getPath().split("/");
        if (parts.length < 4) { ex.sendResponseHeaders(400, -1); return; }
        int id;
        try { id = Integer.parseInt(parts[3]); }
        catch (NumberFormatException nfe) { ex.sendResponseHeaders(400, -1); return; }

        try {
            FoodItem item = svc.get(id);
            if (item == null || item.getStatus() != FoodItem.Status.AVAILABLE) {
                ex.sendResponseHeaders(404, -1);
            } else {
                byte[] resp = JsonUtil.toJsonBytes(item);
                ex.getResponseHeaders().add("Content-Type", "application/json");
                ex.sendResponseHeaders(200, resp.length);
                ex.getResponseBody().write(resp);
            }
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}
