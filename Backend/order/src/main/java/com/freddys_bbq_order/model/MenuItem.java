package com.freddys_bbq_order.model;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String category;

  private String name;

  private double price;

  private String image;

  public MenuItem(String category, String name, double price, String image) {
    this.category = category;
    this.name = name;
    this.price = price;
    this.image = image;
  }

  public MenuItem() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return String.format("MenuItem<id: %s, name: %s, price: %s>", id, name, price);
  }
}
