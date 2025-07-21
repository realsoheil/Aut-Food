package com.autfood.server.controller;

import com.autfood.server.dao.OrderDao;
import com.autfood.server.model.Order;

import java.util.List;

public class OrderService {

    public List<Order> getOrdersByUser(int userId) {
        return OrderDao.getByUserId(userId);
    }

    public Order getOrderById(int id) {
        return OrderDao.findById(id).orElse(null);
    }

    public Order createOrder(int userId, int restaurantId, List<Order.Item> items, String address) {
        int total = 0;
        for (Order.Item item : items) {
            total += 100_000 * item.getQuantity(); // فرض: هر آیتم 100هزار تومان
        }

        int tax = (int) (0.09 * total);
        int deliveryFee = 20_000;
        int finalTotal = total + tax + deliveryFee;

        Order order = new Order();
        order.setId(OrderDao.generateId());
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setItems(items);
        order.setDeliveryAddress(address);
        order.setStatus("در حال بررسی");
        order.setTotalPrice(finalTotal);

        OrderDao.save(order);
        return order;
    }
}
