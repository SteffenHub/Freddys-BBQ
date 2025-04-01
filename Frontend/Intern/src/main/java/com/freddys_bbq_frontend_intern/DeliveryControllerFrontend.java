package com.freddys_bbq_frontend_intern;


import com.freddys_bbq_frontend_intern.model.Delivery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller
@RequestMapping("/intern/delivery")
public class DeliveryControllerFrontend {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    private final RestTemplate restTemplate;

    public DeliveryControllerFrontend(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getDeliveries() {
        try {
            ResponseEntity<Delivery[]> response = restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery", Delivery[].class);
            Delivery[] deliveries = response.getBody();

            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            System.err.println("Error fetching deliveries: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error fetching deliveries");
        }
    }

    @GetMapping
    public String getDeliverySite() {
        return "delivery";
    }

    @PostMapping("/start")
    public ResponseEntity<String> startDelivery(@RequestBody UUID deliveryId) {
        ResponseEntity<String> response = restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery/start", deliveryId, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Delivery started");
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delivered")
    public ResponseEntity<String> markAsDelivered(@RequestBody UUID id) {
        ResponseEntity<String> response = restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery/delivered", id, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Delivery marked as Delivered");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
