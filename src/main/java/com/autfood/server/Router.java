package com.autfood.server.http;

import com.autfood.server.util.HealthHandler;
import com.sun.net.httpserver.HttpServer;

public final class Router {
    private Router() {}
    public static void configure(HttpServer server) {
        // مسیر سلامت
        server.createContext("/health", new HealthHandler());
        server.createContext("/auth/register", new RegisterHandler());
        server.createContext("/auth/login",    new LoginHandler());
        server.createContext("/api/restaurants",       new RestaurantCreateHandler());   // POST
        server.createContext("/api/restaurants/",      new RestaurantGetHandler());      // GET /{id}
        server.createContext("/api/restaurants/list",  new RestaurantListHandler());     // GET
        server.createContext("/api/admin/restaurants/",new RestaurantApproveHandler());  // PUT /{id}/approve
        server.createContext("/api/health", new HealthHandler());
// فایل آپلود
        server.createContext("/api/files", new FileUploadHandler());

// آیتم‌ها
        server.createContext("/api/restaurants/",       new FoodCreateHandler());   // POST   /api/restaurants/{rid}/items
        server.createContext("/api/restaurants/",       new FoodListHandler());     // GET    /api/restaurants/{rid}/items
        server.createContext("/api/items/search",       new FoodSearchHandler());   // GET    /api/items/search?query=...
        server.createContext("/api/items/",             new FoodGetHandler());      // GET    /api/items/{id}




        // بعداً: server.createContext("/users", new UserHandler());
        //        server.createContext("/orders", new OrderHandler());
    }
}
