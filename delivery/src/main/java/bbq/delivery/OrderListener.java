package bbq.delivery;

import bbq.delivery.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final DeliveryRepository deliveryRepository;

    public void onOrder(Order order) {
        log.info("receive order: {}", order);
        this.deliveryRepository.addNewOrder(order);
    }
}
