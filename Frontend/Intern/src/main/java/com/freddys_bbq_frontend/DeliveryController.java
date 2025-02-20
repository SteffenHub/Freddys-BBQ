package com.freddys_bbq_frontend;


import com.freddys_bbq_frontend.model.Delivery;
import com.freddys_bbq_frontend.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    private final RestTemplate restTemplate;

    public DeliveryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping
    public String getAllDeliveries(Model model) {

        ResponseEntity<Delivery[]> response = restTemplate.getForEntity(deliveryBackendUrl + "/delivery", Delivery[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("deliveries", response.getBody());
        }else{
            model.addAttribute("deliveries", new Delivery[]{});
            model.addAttribute("errorMessage", "The Deliveries could not be found or the Backend does not answer");
        }
        return "delivery";
    }
}
