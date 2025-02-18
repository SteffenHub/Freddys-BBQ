package com.freddys_bbq.model;


import java.util.UUID;

public class OrderRequest {

    public OrderRequest(String name, UUID drinkId, UUID foodId) {
        this.name = name;
        this.drinkId = drinkId;
        this.foodID = foodId;
    }

    private final String name;

    private final UUID drinkId;

    private final UUID foodID;

    public String getName() {
        return name;
    }

    public UUID getDrinkId() {
        return drinkId;
    }

    public UUID getFoodId() {
        return foodID;
    }

}
