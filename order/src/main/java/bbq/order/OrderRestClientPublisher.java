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

    private final RestClient restClient;

    @Value("${delivery-service.url:http://localhost:8050}")
    private String deliveryServiceUrl;

    void publish(Order order) {
        var result = restClient.post().
                uri(deliveryServiceUrl + "/api/delivery")
                .body(order)
                .retrieve()
                .toEntity(JsonNode.class);
        log.info("Published order with result {}", result);
    }
}
