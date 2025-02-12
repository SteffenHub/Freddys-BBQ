package bbq.delivery;

import bbq.delivery.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final DeliveryRepository deliveryRepository;

    @RetryableTopic(attempts = "2")
    @KafkaListener(topics = "orders", groupId = "delivery", properties = { "spring.json.value.default.type=bbq.delivery.model.Order"})
    public void onOrder(Order order) {
     //throw new RuntimeException("Order delivery failed");
     log.info("receive order: {}", order);
     deliveryRepository.addNewOrder(order);
    }
}
