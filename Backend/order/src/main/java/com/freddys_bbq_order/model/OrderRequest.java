package com.freddys_bbq_order.model;

import java.util.List;
import java.util.UUID;

public class OrderRequest {

    public OrderRequest(String name, List<UUID> items) {
        this.name = name;
        this.items = items;
    }

    private final String name;

    private final List<UUID> items;

    public List<UUID> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}
