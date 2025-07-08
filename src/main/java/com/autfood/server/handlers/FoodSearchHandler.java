package com.autfood.server.http.handlers;

import com.autfood.server.controller.FoodItemService;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GET /api/items/search?query=term
 */
public class FoodSearchHandler implements HttpHandler {

    private final FoodItemService svc = new FoodItemService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        String query = queryParam(ex.getRequestURI(), "query");
        if (query == null || query.isBlank()) { ex.sendResponseHeaders(400, -1); return; }

        try {
            byte[] resp = JsonUtil.toJsonBytes(svc.search(query));
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(200, resp.length);
            ex.getResponseBody().write(resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }

    /* متد کوچک برای خواندن پارامتر querystring */
    private String queryParam(URI uri, String key) {
        if (uri.getQuery() == null) return null;
        Map<String,String> map =
                List.of(uri.getQuery().split("&")).stream()
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : ""));
        return map.get(key);
    }
}

