package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.Delivery;
import com.freddys_bbq_delivery.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller responsible for managing deliveries in the backend system.
 */
@Tag(name = "Delivery", description = "Handles delivery operations for orders.")
@Controller
@RequestMapping("/api/delivery/delivery")
public class DeliveryControllerBackend {

    private final DeliveryRepository deliveryRepository;

    public DeliveryControllerBackend(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Creates a new delivery for a given order.
     *
     * @param order The order to be delivered.
     * @return ResponseEntity with a success message.
     */
    @Operation(
            summary = "Create a new delivery",
            description = "Creates a new delivery entry in the system for the provided order.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Delivery created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<String> createDelivery(@RequestBody Order order) {
        this.deliveryRepository.addDelivery(new Delivery(order));
        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery created successfully");
    }

    /**
     * Retrieves a delivery based on the provided order ID.
     *
     * @param id The UUID of the order.
     * @return ResponseEntity containing the delivery details.
     */
    @Operation(
            summary = "Retrieve a specific delivery",
            description = "Fetches a delivery associated with the provided order ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Delivery found",
                            content = @Content(schema = @Schema(implementation = Delivery.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid order ID", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getOrder(@PathVariable UUID id) {
        try {
            Delivery delivery = this.deliveryRepository.getDeliveryByOrderId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Order ID"));
            return ResponseEntity.status(HttpStatus.OK).body(delivery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all deliveries.
     *
     * @return ResponseEntity containing an array of all deliveries.
     */
    @Operation(
            summary = "Retrieve all deliveries",
            description = "Fetches all deliveries stored in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of deliveries retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Delivery[].class)))
            }
    )
    @GetMapping
    public ResponseEntity<Delivery[]> getOrder() {
        Delivery[] deliveries = this.deliveryRepository.getDeliveries().toArray(new Delivery[0]);
        return ResponseEntity.status(HttpStatus.OK).body(deliveries);
    }

    /**
     * Starts a delivery for a specific order.
     *
     * @param id The UUID of the order whose delivery should start.
     * @return ResponseEntity with a success message or error message if the order is not found.
     */
    @Operation(
            summary = "Start a delivery",
            description = "Changes the status of a given order's delivery to 'In Delivery'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Delivery started successfully"),
                    @ApiResponse(responseCode = "400", description = "Order not found", content = @Content)
            }
    )
    @PostMapping("/start")
    public ResponseEntity<String> startDelivery(@RequestBody UUID id) {
        Optional<Delivery> delivery = this.deliveryRepository.getDeliveryByOrderId(id);
        if (delivery.isPresent()) {
            delivery.get().setStatus("In Delivery");
            return ResponseEntity.ok("Started Delivery");
        }else{
            return ResponseEntity.badRequest().body("order not found");
        }
    }

    /**
     * Mark a delivery as delivered
     *
     * @param id The UUID of the order whose delivery should mark as delivery.
     * @return ResponseEntity with a success message or error message if the order is not found.
     */
    @Operation(
            summary = "Mark a delivery as delivered",
            description = "Changes the status of a given order's delivery to 'Delivered'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Delivery marked successfully as Delivered"),
                    @ApiResponse(responseCode = "400", description = "Order not found", content = @Content)
            }
    )
    @PostMapping("/delivered")
    public ResponseEntity<String> markAsDelivered(@RequestBody UUID id) {
        Optional<Delivery> delivery = this.deliveryRepository.getDeliveryByOrderId(id);
        if (delivery.isPresent()) {
            delivery.get().setStatus("Delivered");
            return ResponseEntity.ok("Delivery marked as Delivered");
        }else{
            return ResponseEntity.badRequest().body("order not found");
        }
    }
}
