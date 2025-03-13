package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import com.freddys_bbq_order.model.Order;
import com.freddys_bbq_order.model.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Order Management", description = "Endpoints for managing customer orders.")
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

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all orders.
     */
    @Operation(summary = "Get all orders", description = "Returns a list of all placed orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Iterable<Order>> findAll() {
        Iterable<Order> orders = orderRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    /**
     * Places a new order based on the provided order request.
     *
     * @param request The order request containing menu item IDs and customer details.
     * @return The unique identifier (UUID) of the created order.
     */
    @Operation(summary = "Place an order", description = "Creates a new order and forwards it to the delivery service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid order request. Invalid id"),
            @ApiResponse(responseCode = "500", description = "Failed to forward the order to delivery service")
    })
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            // Bestellte Objekte anhand der IDs finden
            MenuItem drink = menuItemRepository.findById(request.getDrinkId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid drink ID"));
            MenuItem meal = menuItemRepository.findById(request.getMealId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid meal ID"));
            MenuItem side = menuItemRepository.findById(request.getSideId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Side ID"));
            Order order = new Order();
            order.setName(request.getName());
            order.setDrink(drink);
            order.setMeal(meal);
            order.setSide(side);
            orderRepository.save(order);

            ResponseEntity<String> response = restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery", order, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
