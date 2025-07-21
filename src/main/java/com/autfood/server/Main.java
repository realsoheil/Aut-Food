package com.autfood.server;

import com.autfood.server.dao.UserDao;
import com.autfood.server.http.Router;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        int port = 8082;  // اگر پورت دیگری می‌خواهی، بگو.
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);

        Router.configure(server);           // همهٔ مسیرها در یک جا ثبت می‌شوند.

        // ThreadPool متناسب با CPU. اگر حجم درخواست زیاد شد، عدد را زیاد می‌کنیم.
        server.setExecutor(Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2));

        server.start();
        System.out.printf("HTTP server started on http://localhost:%d%n", port);
    }
}
