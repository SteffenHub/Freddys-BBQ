package com.freddys_bbq_delivery.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItemD {

  @Id
  private UUID id;

  private String category;

  private String name;

  private double price;

  private String image;

  public MenuItemD() {
  }

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
