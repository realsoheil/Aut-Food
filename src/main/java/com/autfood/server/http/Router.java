package com.autfood.server.http;

import com.autfood.server.handlers.*;
import com.autfood.server.util.HealthHandler;
import com.sun.net.httpserver.HttpServer;

public final class Router {
    private Router() {}

    public static void configure(HttpServer server) {
        // مسیر سلامت
        server.createContext("/api/health", new HealthHandler());

        // احراز هویت
        server.createContext("/auth/register", new com.autfood.server.handlers.RegisterHandler());    // POST
        server.createContext("/auth/login",    new com.autfood.server.http.handlers.LoginHandler());       // POST

        // رستوران‌ها
        server.createContext("/api/restaurants/create",  new RestaurantCreateHandler());    // POST
        server.createContext("/api/restaurants/list",    new RestaurantListHandler());      // GET
        server.createContext("/api/restaurants/get",     new RestaurantGetHandler());       // GET /{id}
        server.createContext("/api/admin/restaurants/approve", new RestaurantApproveHandler()); // PUT /{id}/approve

        // غذاها (items)
        server.createContext("/api/items/create",  new com.autfood.server.http.handlers.FoodCreateHandler());   // POST /api/items/create
        server.createContext("/api/items/list",    new com.autfood.server.handlers.FoodListHandler());     // GET /api/items/list?restaurantId=...
        server.createContext("/api/items/search",  new com.autfood.server.http.handlers.FoodSearchHandler());   // GET /api/items/search?query=...
        server.createContext("/api/items/get",     new com.autfood.server.http.handlers.FoodGetHandler());      // GET /api/items/get?id=...

        // فایل‌ها
        server.createContext("/api/files/upload", new com.autfood.server.http.handlers.FileUploadHandler());
        server.createContext("/api/orders/history", new OrderHistoryHandler());

        // نکته: مسیرهایی که نیاز به پارامتر ID دارند (مانند /api/restaurants/{id})
        // باید در داخل Handler مربوطه با بررسی URI پردازش شوند، نه از طریق context مستقیم.
    }
}

