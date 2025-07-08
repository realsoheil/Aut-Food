package com.autfood.server.http.handlers;

import com.autfood.server.controller.AuthService;
import com.autfood.server.model.User;
import com.autfood.server.util.JsonUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements HttpHandler{
    private final AuthService service=new AuthService();
    @Override public void handle(HttpExchange ex) throws IOException{
        if(!"POST".equals(ex.getRequestMethod())){ex.sendResponseHeaders(405,-1);return;}
        String body=new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject j=JsonUtil.parse(body).getAsJsonObject();
        try{
            User u=service.register(
                    j.get("username").getAsString(),
                    j.get("email").getAsString(),
                    j.get("password").getAsString());
            byte[] resp=JsonUtil.toJsonBytes(u);
            ex.getResponseHeaders().add("Content-Type","application/json");
            ex.sendResponseHeaders(201,resp.length);
            ex.getResponseBody().write(resp);
        }catch(IllegalArgumentException e){error(ex,409,e.getMessage());
        }catch(Exception e){error(ex,500,"internal");}
        ex.close();
    }
    private void error(HttpExchange ex,int code,String msg)throws IOException{
        JsonObject j=new JsonObject();j.addProperty("error",msg);
        byte[] r=JsonUtil.toJsonBytes(j);
        ex.getResponseHeaders().add("Content-Type","application/json");
        ex.sendResponseHeaders(code,r.length);
        ex.getResponseBody().write(r);
    }
}
