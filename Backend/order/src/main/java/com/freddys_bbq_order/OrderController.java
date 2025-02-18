package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import com.freddys_bbq_order.model.Order;
import com.freddys_bbq_order.model.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final MenuItemRepository menuItemRepository;

    private final OrderRepository orderRepository;

    public OrderController(MenuItemRepository menuItemRepository, OrderRepository orderRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        try {
            Order order = this.orderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Order ID"));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
