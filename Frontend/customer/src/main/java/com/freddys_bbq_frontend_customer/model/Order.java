package com.freddys_bbq_frontend_customer.model;

import java.util.UUID;

public class Order {

    private UUID id;

    private String name;

    private MenuItem drink;

    private MenuItem meal;

    private MenuItem side;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItem getDrink() {
        return drink;
    }

    public void setDrink(MenuItem drink) {
        this.drink = drink;
    }

    public MenuItem getMeal() {
        return meal;
    }

    public void setMeal(MenuItem meal) {
        this.meal = meal;
    }

    public MenuItem getSide() {
        return side;
    }

    public void setSide(MenuItem side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("Order<id: %s, drink: %s, meal: %s>", id, meal, drink);
    }

}