package com.freddys_bbq_frontend_customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freddys_bbq_frontend_customer.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Controller
@RequestMapping("/")
public class MenuControllerFrontend {

    private final RestTemplate restTemplate;

    @Value("${ORDER_BACKEND_URL:http://localhost:8080}")
    private String orderBackendUrl;

    @Autowired
    public MenuControllerFrontend(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String index(Model model) {
        try {
            ResponseEntity<MenuItem[]> response = restTemplate.getForEntity(orderBackendUrl + "/api/order/menu?category=Drink", MenuItem[].class);
            Iterable<MenuItem> drinks = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
            response = restTemplate.getForEntity(orderBackendUrl + "/api/order/menu?category=Main Course", MenuItem[].class);
            Iterable<MenuItem> meals = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
            response = restTemplate.getForEntity(orderBackendUrl + "/api/order/menu?category=Side", MenuItem[].class);
            Iterable<MenuItem> sides = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();

            model.addAttribute("drinks", drinks);
            model.addAttribute("meals", meals);
            model.addAttribute("sides", sides);

        } catch (RestClientException e) {
            model.addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
            model.addAttribute("drinks", Collections.emptyList());
            model.addAttribute("meals", Collections.emptyList());
            model.addAttribute("sides", Collections.emptyList());
        }
        return "index";
    }

    @GetMapping("/validate-id")
    public ResponseEntity<Boolean> validateId(@RequestParam("itemId") UUID itemId) {
        // TODO test
        ResponseEntity<Boolean> response = restTemplate.getForEntity(orderBackendUrl + "/api/order/menu/validate-id?id=" + itemId, Boolean.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.internalServerError().build();
        }
        return response;
    }

}