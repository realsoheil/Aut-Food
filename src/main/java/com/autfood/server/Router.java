package com.autfood.server.http;

import com.autfood.server.http.handlers.HealthHandler;
import com.sun.net.httpserver.HttpServer;

public final class Router {
    private Router() {}
    public static void configure(HttpServer server) {
        // مسیر سلامت
        server.createContext("/health", new HealthHandler());

        // بعداً: server.createContext("/users", new UserHandler());
        //        server.createContext("/orders", new OrderHandler());
    }
}
