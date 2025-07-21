package com.autfood.server.handlers;

import com.autfood.server.controller.AuthService;
import com.autfood.server.controller.RestaurantService;
import com.autfood.server.model.Role;
import com.autfood.server.util.TokenUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class RestaurantApproveHandler implements HttpHandler {
    private final AuthService auth = new AuthService();
    private final RestaurantService svc = new RestaurantService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"PUT".equalsIgnoreCase(ex.getRequestMethod())) {
            respondError(ex, 405, "method not allowed");
            return;
        }

        // استخراج توکن و بررسی نقش
        Integer adminId = TokenUtil.extractUserId(ex.getRequestHeaders().getFirst("Authorization"), auth);
        if (adminId == null) {
            respondError(ex, 401, "unauthorized");
            return;
        }

        try {
            if (auth.getUserRole(adminId) != Role.ADMIN) {
                respondError(ex, 403, "forbidden");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // استخراج id از URI
        String[] parts = ex.getRequestURI().getPath().split("/");
        if (parts.length != 6 || !"approve".equals(parts[5])) {
            respondError(ex, 400, "invalid path");
            return;
        }

        try {
            int id = Integer.parseInt(parts[4]);
            svc.approve(id);
            ex.sendResponseHeaders(204, -1); // No Content
        } catch (NumberFormatException e) {
            respondError(ex, 400, "invalid id");
        } catch (Exception e) {
            respondError(ex, 500, "internal server error");
        } finally {
            ex.close();
        }
    }

    private void respondError(HttpExchange ex, int code, String msg) throws IOException {
        JsonObject j = new JsonObject();
        j.addProperty("error", msg);
        byte[] r = j.toString().getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, r.length);
        ex.getResponseBody().write(r);
        ex.getResponseBody().close();
    }
}

