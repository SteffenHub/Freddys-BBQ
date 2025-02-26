package com.freddys_bbq_frontend;

import com.freddys_bbq_frontend.model.MenuItem;
import com.freddys_bbq_frontend.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.*;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/order")
public class OrderController {

  private final RestTemplate restTemplate;

  @Autowired
  public OrderController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Value("${ORDER_BACKEND_URL:http://localhost:8080}")
  private String backendUrl;

  @GetMapping("/{id}")
  public String getOrder(@PathVariable UUID id, Model model) {
    ResponseEntity<Order> response = restTemplate.getForEntity(backendUrl + "/orders/" + id, Order.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      model.addAttribute("order", response.getBody());
    }else{
      model.addAttribute("order", new Order());
      model.addAttribute("errorMessage", "The order could not be found or the Backend does not answer");
    }
    return "order-info";
  }

  @GetMapping
  public String showOrderForm(Model model) {

    try {
      ResponseEntity<MenuItem[]> response = restTemplate.getForEntity(backendUrl + "/menu-items?drink=true", MenuItem[].class);
      Iterable<MenuItem> drinks = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
      response = restTemplate.getForEntity(backendUrl + "/menu-items?drink=false", MenuItem[].class);
      Iterable<MenuItem> foods = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();

      model.addAttribute("drinks", drinks);
      model.addAttribute("foods", foods);

    } catch (RestClientException e) {
      model.addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
      model.addAttribute("drinks", Collections.emptyList());
      model.addAttribute("foods", Collections.emptyList());
    }

    return "order";
  }


  @PostMapping
  public String placeOrder(@RequestParam String name,
                           @RequestParam UUID drinkId,
                           @RequestParam UUID foodId,
                           Model model) {
    try {
      // JSON-Objekt für Bestellung erstellen
      Map<String, Object> orderRequest = new HashMap<>();
      orderRequest.put("name", name);
      orderRequest.put("drinkId", drinkId);
      orderRequest.put("foodId", foodId);

      // POST-Request an das Backend senden
      ResponseEntity<UUID> response = restTemplate.postForEntity(
              backendUrl + "/orders",
              orderRequest,
              UUID.class
      );

      if (response.getStatusCode().is2xxSuccessful()) {
        UUID orderId = response.getBody();
        return "redirect:/delivery/" + orderId;
      }
    } catch (RestClientException e) {
      //model.addAttribute("errorMessage", "Bestellung konnte nicht verarbeitet werden. Bitte versuchen Sie es erneut.");
    }
    model.addAttribute("errorMessage", "The Order cannot be placed");
    return "order";
  }

}
