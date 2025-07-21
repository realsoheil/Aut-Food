package com.autfood.server.model;

public class Restaurant {
    private Integer id;
    private int ownerId;
    private String name;
    private Status status;
    private String description;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED // Optional: add more if needed
    }

    public Restaurant(Integer id, int ownerId, String name, Status status, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

