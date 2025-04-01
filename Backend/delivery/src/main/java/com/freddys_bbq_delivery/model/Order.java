package com.freddys_bbq_delivery.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "bbq_order")
public class Order {

  @Id
  private UUID id;

  private String name;

  @ManyToMany
  @JoinTable(
          name = "order_items",
          joinColumns = @JoinColumn(name = "order_id"),
          inverseJoinColumns = @JoinColumn(name = "menu_item_id")
  )
  private List<MenuItem> items;

  public Order() {
    this.items = new ArrayList<>();
  }

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