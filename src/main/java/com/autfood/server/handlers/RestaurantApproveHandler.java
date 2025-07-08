package com.autfood.server.handlers;

import com.autfood.server.controller.*;
import com.autfood.server.model.Role;
import com.autfood.server.util.TokenUtil;
import com.sun.net.httpserver.*;

import java.io.IOException;

/** PUT /api/admin/restaurants/{id}/approve  (فقط ADMIN) */
public class RestaurantApproveHandler implements HttpHandler {

    private final AuthService auth = new AuthService();
    private final RestaurantService svc = new RestaurantService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"PUT".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }

        // احراز هویت
        Integer adminId = TokenUtil.extractUserId(ex.getRequestHeaders().getFirst("Authorization"), auth);
        if (adminId == null) { ex.sendResponseHeaders(401, -1); return; }

        // نقش باید ADMIN باشد
        if (auth.getUserRole(adminId) != Role.ADMIN) { ex.sendResponseHeaders(403, -1); return; }

        // استخراج id رستوران
        String path = ex.getRequestURI().getPath();      // /api/admin/restaurants/5/approve
        String idStr = path.split("/")[4];               // بخش پنجم
        try {
            int id = Integer.parseInt(idStr);
            svc.approve(id);
            ex.sendResponseHeaders(204, -1);             // No Content
        } catch (NumberFormatException e) {
            ex.sendResponseHeaders(400, -1);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, -1);
        } finally { ex.close(); }
    }
}
