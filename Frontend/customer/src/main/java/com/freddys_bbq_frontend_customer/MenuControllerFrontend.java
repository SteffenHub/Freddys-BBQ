package com.freddys_bbq_frontend_customer;

import com.freddys_bbq_frontend_customer.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/")
public class MenuControllerFrontend {

  private final RestTemplate restTemplate;

  @Value("${ORDER_BACKEND_URL:http://localhost:8080}")
  private String backendUrl;

  @Autowired
  public MenuControllerFrontend(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String index(Model model) {
    try {
      ResponseEntity<MenuItem[]> response = restTemplate.getForEntity(this.backendUrl + "/menu", MenuItem[].class);

      List<MenuItem> menuItems = response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();

      model.addAttribute("menuItems", menuItems);

    } catch (RestClientException e) {
      model.addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
      model.addAttribute("menuItems", Collections.emptyList());
    }

    return "index";
  }

}