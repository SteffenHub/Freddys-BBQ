package bbq.delivery;

import bbq.delivery.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final DeliveryRepository deliveryRepository;

    @RabbitListener(queues = "delivery.orders")
    public void onOrder(Order order) {
        log.info("receive order: {}", order);
        this.deliveryRepository.addNewOrder(order);
    }
}
