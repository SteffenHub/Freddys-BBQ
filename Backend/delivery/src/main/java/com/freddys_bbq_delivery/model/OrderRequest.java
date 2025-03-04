package com.freddys_bbq_delivery.model;


import java.util.UUID;

public class OrderRequest {

    public OrderRequest(String name, UUID drinkId, UUID mealId, UUID sideId) {
        this.name = name;
        this.drinkId = drinkId;
        this.mealId = mealId;
        this.sideId = sideId;
    }

    private final String name;

    private final UUID drinkId;

    private final UUID mealId;

    private final UUID sideId;

    public UUID getSideId() {
        return sideId;
    }

    public String getName() {
        return name;
    }

    public UUID getDrinkId() {
        return drinkId;
    }

    public UUID getMealId() {
        return mealId;
    }
}
