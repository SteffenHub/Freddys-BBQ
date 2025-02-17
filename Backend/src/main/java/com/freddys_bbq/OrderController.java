package com.freddys_bbq;
import com.freddys_bbq.model.MenuItem;
import com.freddys_bbq.model.Order;
import com.freddys_bbq.model.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private MenuItemRepository menuItemRepository;

  @Autowired
  private OrderRepository orderRepository;

  @PostMapping
  public ResponseEntity<UUID> placeOrder(@RequestBody OrderRequest request) {
    try {
      // Bestellte Objekte anhand der IDs finden
      MenuItem drink = menuItemRepository.findById(request.getDrinkId())
              .orElseThrow(() -> new IllegalArgumentException("Invalid drink ID"));
      MenuItem food = menuItemRepository.findById(request.getFoodId())
              .orElseThrow(() -> new IllegalArgumentException("Invalid food ID"));

      Order order = new Order();
      order.setId(UUID.randomUUID());
      order.setName(request.getName());
      order.setDrink(drink);
      order.setFood(food);
      orderRepository.save(order);

      return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
