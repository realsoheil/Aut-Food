package com.autfood.server.dao;

import com.autfood.server.model.Order;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderDao {
    private static final String FILE_PATH = "data/orders.json";
    private static final Gson gson = new Gson();
    private static final Type LIST_TYPE = new TypeToken<List<Order>>() {}.getType();
    private static List<Order> cache = new ArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger(1);

    static {
        load();
        for (Order o : cache)
            counter.set(Math.max(counter.get(), o.getId() + 1));
    }

    private static void load() {
        try (Reader reader = new InputStreamReader(new FileInputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            cache = gson.fromJson(reader, LIST_TYPE);
            if (cache == null) cache = new ArrayList<>();
        } catch (IOException e) {
            cache = new ArrayList<>();
        }
    }

    private static void saveAll() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            gson.toJson(cache, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int generateId() {
        return counter.getAndIncrement();
    }

    public static void save(Order order) {
        cache.add(order);
        saveAll();
    }

    public static List<Order> getByUserId(int userId) {
        List<Order> result = new ArrayList<>();
        for (Order o : cache)
            if (o.getUserId() == userId) result.add(o);
        return result;
    }

    public static Optional<Order> findById(int id) {
        return cache.stream().filter(o -> o.getId() == id).findFirst();
    }

    public static List<Order> getAll() {
        return new ArrayList<>(cache);
    }
}

