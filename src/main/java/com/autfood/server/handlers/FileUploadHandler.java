package com.autfood.server.http.handlers;

import com.google.gson.JsonObject;
import com.autfood.server.util.JsonUtil;
import com.sun.net.httpserver.*;

import java.io.*;
import java.nio.file.*;

public class FileUploadHandler implements HttpHandler {
    private static final Path UPLOAD_DIR = Paths.get("uploads");

    @Override public void handle(HttpExchange ex)throws IOException {
        if(!"POST".equals(ex.getRequestMethod())){ex.sendResponseHeaders(405,-1);return;}
        Files.createDirectories(UPLOAD_DIR);
        // فرض: کل بدنه یک فایل باینری است (برای سادگی)
        String fileName = "img_" + System.currentTimeMillis() + ".bin";
        Path target = UPLOAD_DIR.resolve(fileName);
        Files.copy(ex.getRequestBody(), target, StandardCopyOption.REPLACE_EXISTING);

        JsonObject j = new JsonObject();
        j.addProperty("path", target.toString());
        byte[] resp = JsonUtil.toJsonBytes(j);
        ex.getResponseHeaders().add("Content-Type","application/json");
        ex.sendResponseHeaders(201, resp.length);
        ex.getResponseBody().write(resp);
        ex.close();
    }
}
