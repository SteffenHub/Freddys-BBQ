package bbq.order;

import bbq.order.model.Order;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderRestTemplatePublisher {

    @Value("${kitchen-service.url:http://localhost:8070}")
    private String kitchenServiceUrl;

    @Value("${delivery-service.url:http://localhost:8050}")
    private String deliveryServiceUrl;

    private final RestTemplate restTemplate;

    void publish(Order order) {
        var responseDelivery = restTemplate.postForObject(deliveryServiceUrl + "/api/delivery", order, JsonNode.class);
        log.info("Published to delivery with response: {}", responseDelivery);

        var responseKitchen = restTemplate.postForObject(kitchenServiceUrl + "/api/kitchen", order, String.class);
        log.info("Published to delivery with response: {}", responseKitchen);
    }

}
