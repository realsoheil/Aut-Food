package com.autfood.server.http.handlers;

import com.autfood.server.util.JsonUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HealthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("status", "UP");

        byte[] body = JsonUtil.toJsonBytes(json);

        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(200, body.length);
        ex.getResponseBody().write(body);
        ex.close();
    }
}
