package com.autfood.server.handlers;

import com.autfood.server.controller.AuthService;
import com.autfood.server.model.User;
import com.autfood.server.util.JsonUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements HttpHandler {
    private final AuthService service = new AuthService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            return;
        }

        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject j;

        try {
            j = JsonUtil.parse(body).getAsJsonObject();
            if (!j.has("username") || !j.has("email") || !j.has("password")) {
                error(ex, 400, "Missing required fields");
                return;
            }

            User u = service.register(
                    j.get("username").getAsString(),
                    j.get("email").getAsString(),
                    j.get("password").getAsString());

            byte[] resp = JsonUtil.toJsonBytes(u);
            ex.getResponseHeaders().set("Content-Type", "application/json");
            ex.sendResponseHeaders(201, resp.length);
            ex.getResponseBody().write(resp);
        } catch (IllegalArgumentException e) {
            error(ex, 409, e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            error(ex, 500, "internal");
        } finally {
            ex.getResponseBody().close(); // مهم!
            ex.close();
        }
    }

    private void error(HttpExchange ex, int code, String msg) throws IOException {
        JsonObject j = new JsonObject();
        j.addProperty("error", msg);
        byte[] r = JsonUtil.toJsonBytes(j);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, r.length);
        ex.getResponseBody().write(r);
    }
}
