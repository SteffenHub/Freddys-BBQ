package com.freddys_bbq_frontend_intern.model;


import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;

    private List<MenuItem> items;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order<id: " + id + ", customer: " + name + ", items: " + items + ">";
    }
}