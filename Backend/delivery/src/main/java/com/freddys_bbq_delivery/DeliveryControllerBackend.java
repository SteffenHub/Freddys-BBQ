package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.Delivery;
import com.freddys_bbq_delivery.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/delivery")
public class DeliveryControllerBackend {

    private final DeliveryRepository deliveryRepository;

    public DeliveryControllerBackend(DeliveryRepository deliveryRepository) {
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

    @GetMapping
    public ResponseEntity<Delivery[]> getOrder() {
        Delivery[] deliveries = this.deliveryRepository.getDeliveries().toArray(new Delivery[0]);
        return ResponseEntity.status(HttpStatus.OK).body(deliveries);
    }

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
}
