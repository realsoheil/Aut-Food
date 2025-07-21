package com.autfood.server.handlers;

import com.autfood.server.controller.AuthService;
import com.autfood.server.controller.OrderService;
import com.autfood.server.model.Order;
import com.autfood.server.model.Role;
import com.autfood.server.util.JsonUtil;
import com.autfood.server.util.TokenUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class OrderHistoryHandler implements HttpHandler {
    private final AuthService auth = new AuthService();
    private final OrderService orderService = new OrderService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            respondError(ex, 405, "method not allowed");
            return;
        }

        Integer userId = TokenUtil.extractUserId(ex.getRequestHeaders().getFirst("Authorization"), auth);
        try {
            if (userId == null || auth.getUserRole(userId) != Role.BUYER) {
                respondError(ex, 401, "unauthorized or forbidden");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Order> orders = orderService.getOrdersByUser(userId);
        byte[] response = JsonUtil.toJsonBytes(orders);

        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, response.length);
        ex.getResponseBody().write(response);
        ex.getResponseBody().close();
        ex.close();
    }

    private void respondError(HttpExchange ex, int code, String msg) throws IOException {
        String json = "{\"error\":\"" + msg + "\"}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }
}
