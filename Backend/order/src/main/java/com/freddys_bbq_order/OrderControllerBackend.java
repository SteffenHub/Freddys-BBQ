package com.freddys_bbq_order;

import com.freddys_bbq_order.model.EmailDetails;
import com.freddys_bbq_order.model.MenuItemO;
import com.freddys_bbq_order.model.OrderO;
import com.freddys_bbq_order.model.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Order Management", description = "Endpoints for managing customer orders.")
@Controller
@RequestMapping("/api/order/orders")
public class OrderControllerBackend {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    @Value("${MAIL_BACKEND_URL:http://localhost:8010}")
    private String mailBackendUrl;

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
    public ResponseEntity<Iterable<OrderO>> findAll() {
        Iterable<OrderO> orders = orderRepository.findAll();
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
        if (request.getName() == null || request.getEmail() == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            // find the ordered menu items by id
            List<MenuItemO> items = new ArrayList<>();
            for (UUID itemId : request.getItems()) {
                Optional<MenuItemO> item = menuItemRepository.findById(itemId);
                if (item.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                items.add(item.get());
            }
            OrderO order = new OrderO();
            order.setName(request.getName());
            order.setItems(items);
            orderRepository.save(order);

            ResponseEntity<String> response = restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery", order, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(request.getEmail());
            emailDetails.setSubject("Freddy Comes to your House!");
            StringBuilder message = new StringBuilder();
            message.append(order.getName()).append(", Thank you for your order at Freddy's BBQ! \n \n");
            for (MenuItemO item : order.getItems()) {
                message.append(item.getName()).append("\n");
            }
            emailDetails.setMsgBody(message.toString());

            restTemplate.postForEntity(mailBackendUrl + "/sendMail", emailDetails, String.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
