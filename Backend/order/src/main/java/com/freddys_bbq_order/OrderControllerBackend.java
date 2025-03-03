package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import com.freddys_bbq_order.model.Order;
import com.freddys_bbq_order.model.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/order/orders")
public class OrderControllerBackend {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    private final MenuItemRepository menuItemRepository;

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate;

    public OrderControllerBackend(MenuItemRepository menuItemRepository, OrderRepository orderRepository, RestTemplate restTemplate) {
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> findAll() {
        Iterable<Order> orders = orderRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
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


            ResponseEntity<String> response = restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery", order, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("The order could not be forwarded");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
