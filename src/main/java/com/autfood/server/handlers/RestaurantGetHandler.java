package com.autfood.server.handlers;

import com.autfood.server.controller.RestaurantService;
import com.autfood.server.model.Restaurant;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;

/** GET /api/restaurants/{id}  (عموم می‌توانند جزئیات رستوران تأیید‌شده را ببینند) */
public class RestaurantGetHandler implements HttpHandler {

    private final RestaurantService svc = new RestaurantService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        String path = ex.getRequestURI().getPath();          // /api/restaurants/123
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        try {
            int id = Integer.parseInt(idStr);
            Restaurant r = svc.get(id);

            if (r == null || r.getStatus() != Restaurant.Status.APPROVED) {
                ex.sendResponseHeaders(404, -1);
            } else {
                byte[] resp = JsonUtil.toJsonBytes(r);
                ex.getResponseHeaders().add("Content-Type", "application/json");
                ex.sendResponseHeaders(200, resp.length);
                ex.getResponseBody().write(resp);
            }
        } catch (NumberFormatException e) {
            ex.sendResponseHeaders(400, -1);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}
