package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.DeliveryD;
import com.freddys_bbq_delivery.model.OrderD;
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
    public ResponseEntity<String> createDelivery(@RequestBody OrderD order) {
        this.deliveryRepository.save(new DeliveryD(order));
        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery created successfully");
    }

    /**
     * Retrieves a delivery based on the provided order ID.
     *
     * @param orderId The UUID of the order.
     * @return ResponseEntity containing the delivery details.
     */
    @Operation(
            summary = "Retrieve a specific delivery",
            description = "Fetches a delivery associated with the provided order ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Delivery found",
                            content = @Content(schema = @Schema(implementation = DeliveryD.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid order ID", content = @Content)
            }
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<DeliveryD> getOrder(@PathVariable UUID orderId) {
        try {
            DeliveryD delivery = this.deliveryRepository.findByOrderId(orderId)
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
                            content = @Content(schema = @Schema(implementation = DeliveryD[].class)))
            }
    )
    @GetMapping
    public ResponseEntity<DeliveryD[]> getOrder() {
        DeliveryD[] deliveries = this.deliveryRepository.findAll().toArray(new DeliveryD[0]);
        return ResponseEntity.status(HttpStatus.OK).body(deliveries);
    }

    /**
     * Starts a delivery for a specific order.
     *
     * @param deliveryId The UUID of the order whose delivery should start.
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
    public ResponseEntity<String> startDelivery(@RequestBody UUID deliveryId) {
        Optional<DeliveryD> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isPresent()) {
            DeliveryD delivery = deliveryOpt.get();
            delivery.setStatus("In Delivery");

            deliveryRepository.save(delivery);

            return ResponseEntity.ok("Started Delivery");
        } else {
            return ResponseEntity.badRequest().body("Delivery not found");
        }
    }

    /**
     * Mark a delivery as delivered
     *
     * @param deliveryId The UUID of the order whose delivery should mark as delivery.
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
    public ResponseEntity<String> markAsDelivered(@RequestBody UUID deliveryId) {
        Optional<DeliveryD> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isPresent()) {
            DeliveryD delivery = deliveryOpt.get();
            delivery.setStatus("Delivered");

            deliveryRepository.save(delivery);

            return ResponseEntity.ok("Delivery marked as Delivered");
        }else{
            return ResponseEntity.badRequest().body("order not found");
        }
    }
}
