package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.Delivery;
import com.freddys_bbq_delivery.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    public ResponseEntity<String> createDelivery(@RequestBody Order order) {
        this.deliveryRepository.addDelivery(new Delivery(order));
        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery created successfully");
    }

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
}
