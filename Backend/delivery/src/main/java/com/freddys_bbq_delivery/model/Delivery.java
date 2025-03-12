package com.freddys_bbq_delivery.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Delivery {

    private final Order order;

    private String status;

    @JsonCreator
    public Delivery(@JsonProperty("order") Order order) {
        this.order = order;
        this.status = "Received";
    }

    public Order getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Delivery<status: %s, order: %s>", status, order.toString());
    }
}
