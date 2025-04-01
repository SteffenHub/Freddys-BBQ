package com.freddys_bbq_frontend_intern.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Delivery {

    private UUID id;

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

    public UUID getId() {
        return id;
    }
}
