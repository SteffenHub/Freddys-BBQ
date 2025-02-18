package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.Order;

import java.util.ArrayList;
import java.util.List;


public class OrderRepository{

    private final List<Order> orders;

    public OrderRepository(){
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders(){
        return this.orders;
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }

    public void removeOrder(Order order){
        this.orders.remove(order);
    }

}