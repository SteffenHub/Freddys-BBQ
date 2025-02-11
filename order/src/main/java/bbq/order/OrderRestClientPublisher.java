package bbq.order;

import bbq.order.model.Order;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderRestClientPublisher {

    @Value("${kitchen-service.url:http://localhost:8070}")
    private String kitchenServiceUrl;

    @Value("${delivery-service.url:http://localhost:8050}")
    private String deliveryServiceUrl;

    private final RestClient restClient;

    void publish(Order order) {
        var responseDelivery = restClient.post()
                .uri(deliveryServiceUrl + "/api/delivery")
                .body(order)
                .retrieve()
                .toEntity(JsonNode.class);
        log.info("Published to delivery with response: {}", responseDelivery);

        var responseKitchen = restClient.post()
                .uri(kitchenServiceUrl + "/api/kitchen")
                .body(order)
                .retrieve()
                .toEntity(String.class);
        log.info("Published to delivery with response: {}", responseKitchen);

    }

}
