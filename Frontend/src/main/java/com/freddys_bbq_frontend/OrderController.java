package com.freddys_bbq_frontend;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private MenuItemRepository menuItemRepository;

  @Autowired
  private OrderRepository orderRepository;

  @GetMapping
  public String showOrderForm(Model model) {
    // Liste der Getränke und Speisen laden
    Iterable<MenuItem> drinks = menuItemRepository.findByDrinkOrderByNameDesc(true);
    Iterable<MenuItem> foods = menuItemRepository.findByDrinkOrderByNameDesc(false);

    // Füge die Listen zum Model hinzu
    model.addAttribute("drinks", drinks);
    model.addAttribute("foods", foods);

    return "order";
  }

  @PostMapping
  public String placeOrder(@RequestParam String name,
      @RequestParam UUID drinkId,
      @RequestParam UUID foodId,
      Model model) {

    // Bestellte Objekte anhand der IDs finden
    MenuItem drink = menuItemRepository.findById(drinkId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid drink ID"));
    MenuItem food = menuItemRepository.findById(foodId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid food ID"));

    // Bestellung erstellen und speichern
    Order order = new Order();
    order.setId(UUID.randomUUID());
    order.setName(name);
    order.setDrink(drink);
    order.setFood(food);
    orderRepository.save(order);

    // Füge die Listen zum Model hinzu
    model.addAttribute("name", name);
    model.addAttribute("drink", drink);
    model.addAttribute("food", food);

    return "order-success"; // Erfolgsseite
  }
}
