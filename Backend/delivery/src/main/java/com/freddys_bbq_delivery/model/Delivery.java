package com.freddys_bbq_delivery.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    private String status;

    public Delivery() {
        this.status = "Received";
    }

    public Delivery(Order order) {
        this.order = order;
        this.status = "Received";
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Delivery<id: %s, status: %s, order: %s>", id, status, order);
    }
}
