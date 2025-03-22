package com.freddys_bbq_frontend_customer;

import com.freddys_bbq_frontend_customer.model.MenuItem;
import com.freddys_bbq_frontend_customer.model.Order;
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
public class OrderControllerFrontend {

  private final RestTemplate restTemplate;

  @Autowired
  public OrderControllerFrontend(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Value("${ORDER_BACKEND_URL:http://localhost:8080}")
  private String orderBackendUrl;

  @GetMapping("/{id}")
  public String getOrder(@PathVariable UUID id, Model model) {
    ResponseEntity<Order> response = restTemplate.getForEntity(orderBackendUrl + "/api/order/orders/" + id, Order.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      model.addAttribute("order", response.getBody());
    }else{
      model.addAttribute("order", new Order());
      model.addAttribute("errorMessage", "The order could not be found or the Backend does not answer");
    }
    return "order-info";
  }

  @GetMapping
  public String showOrderForm(@RequestParam(required = false) List<UUID> items, Model model) {
    try {
      if (items != null || !items.isEmpty()) {
        List<MenuItem> selectedItems = new ArrayList<>();
        for (UUID id : items) {
          ResponseEntity<MenuItem> response = restTemplate.getForEntity(orderBackendUrl + "/api/order/menu?id=" + id, MenuItem.class);
          // TODO handle not successful
          if (response.getStatusCode().is2xxSuccessful()) {
            selectedItems.add(response.getBody());
          }
        }
        model.addAttribute("cartItems", selectedItems);
      } else {
        model.addAttribute("cartItems", new ArrayList<>());
        model.addAttribute("errorMessage", "The Cart is empty");
      }
    } catch (RestClientException e) {
      model.addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
      model.addAttribute("cartItems", new ArrayList<>());
    }

    return "order";
  }


  @PostMapping
  public String placeOrder(@RequestParam String name,
                           @RequestParam UUID drinkId,
                           @RequestParam UUID mealId,
                           @RequestParam UUID sideId,
                           Model model) {
    try {
      // JSON-Objekt für Bestellung erstellen
      Map<String, Object> orderRequest = new HashMap<>();
      orderRequest.put("name", name);
      orderRequest.put("drinkId", drinkId);
      orderRequest.put("mealId", mealId);
      orderRequest.put("sideId", sideId);

      // POST-Request an das Backend senden
      ResponseEntity<UUID> response = restTemplate.postForEntity(
              orderBackendUrl + "/api/order/orders",
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
