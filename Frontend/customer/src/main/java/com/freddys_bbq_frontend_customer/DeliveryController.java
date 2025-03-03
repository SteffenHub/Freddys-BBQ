package com.freddys_bbq_frontend_customer;


import com.freddys_bbq_frontend_customer.model.Delivery;
import com.freddys_bbq_frontend_customer.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    private final RestTemplate restTemplate;

    public DeliveryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("status/{id}")
    public ResponseEntity<String> getDeliveryStatus(@PathVariable UUID id) {
        ResponseEntity<Delivery> response = restTemplate.getForEntity(deliveryBackendUrl + "/delivery/" + id, Delivery.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"status\": \"" + Objects.requireNonNull(response.getBody()).getStatus() + "\"}");
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable UUID id, Model model) {

        ResponseEntity<Delivery> response = restTemplate.getForEntity(deliveryBackendUrl + "/delivery/" + id, Delivery.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("delivery", response.getBody());
        }else{
            model.addAttribute("delivery", new Delivery(new Order()));
            model.addAttribute("errorMessage", "The Order could not be found or the Backend does not answer");
        }
        return "order-info";
    }
}
