package com.autfood.server.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Order {
    private int id;
    private int userId;
    private int restaurantId;
    private List<Item> items;
    private String deliveryAddress;
    private int totalPrice;
    private String status;

    public static class Item {
        private int itemId;
        private int quantity;

        public Item(int itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public int getItemId() { return itemId; }
        public int getQuantity() { return quantity; }
    }

    // Getters and Setters ...
}

