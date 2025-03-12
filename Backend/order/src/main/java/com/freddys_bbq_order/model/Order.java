package com.freddys_bbq_order.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity()
@Table(name = "bbq_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @ManyToOne()
  @JoinColumn(name = "drink_id")
  private MenuItem drink;

  @ManyToOne()
  @JoinColumn(name = "meal_id")
  private MenuItem meal;

  @ManyToOne()
  @JoinColumn(name = "side_id")
  private MenuItem side;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MenuItem getDrink() {
    return drink;
  }

  public void setDrink(MenuItem drink) {
    this.drink = drink;
  }

  public MenuItem getMeal() {
    return meal;
  }

  public void setMeal(MenuItem meal) {
    this.meal = meal;
  }

  public MenuItem getSide() {
    return side;
  }

  public void setSide(MenuItem side) {
    this.side = side;
  }

  @Override
  public String toString() {
    return String.format("Order<id: %s, customer: %s, drink: %s, meal: %s, side: %s>", id, name, drink, meal, side);
  }

}