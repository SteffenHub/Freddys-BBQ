package com.freddys_bbq_order.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity()
@Table(name = "bbq_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @ManyToMany
  @JoinTable(
          name = "order_items",
          joinColumns = @JoinColumn(name = "order_id"),
          inverseJoinColumns = @JoinColumn(name = "menu_item_id")
  )
  private List<MenuItem> items;

  public List<MenuItem> getItems() {
    return items;
  }

  public void setItems(List<MenuItem> items) {
    this.items = items;
  }

  public UUID getId() {
    return id;
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