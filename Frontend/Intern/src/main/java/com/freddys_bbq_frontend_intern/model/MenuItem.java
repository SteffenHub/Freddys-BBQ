package com.freddys_bbq_frontend_intern.model;

import java.util.UUID;


public class MenuItem {

    private UUID id;

    private String category;

    private String name;

    private double price;

    private boolean drink = false;

    private String image;

    public MenuItem() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDrink() {
        return drink;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return String.format("MenuItem<id: %s, name: %s, price: %s>", id, name, price);
    }
}
