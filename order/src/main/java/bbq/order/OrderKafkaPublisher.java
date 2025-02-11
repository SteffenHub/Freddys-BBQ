package bbq.order;

import bbq.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaPublisher {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    void publish(Order order) {
        kafkaTemplate.send("orders", order);
    }

}
