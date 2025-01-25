package bbq.delivery;

import bbq.delivery.model.Delivery;
import bbq.delivery.model.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryRepository {

    private final List<Delivery> deliveries = new ArrayList<>();

    public Delivery addNewOrder(Order order) {
        var delivery = Delivery.builder()
                .receivedAt(LocalDateTime.now())
                .status("Received")
                .progress(10)
                .order(order)
                .build();
        deliveries.add(delivery);
        return delivery;
    }

    public List<Delivery> getAll() {
        return deliveries;
    }

    public Optional<Delivery> findByOrderId(String orderId) {
        return deliveries.stream()
                .filter(delivery -> delivery.getOrder().getId().equals(orderId))
                .findFirst();
    }

}
