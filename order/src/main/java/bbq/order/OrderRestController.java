package bbq.order;

import bbq.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final OrderRestClientPublisher publisher;

    @GetMapping
    public List<Order> get() {
        return orderRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order post(@RequestBody Order order) {
        // 1. Save Order
        var savedOrder = orderRepository.save(order);

        // 2. Publish order
        publisher.publish(savedOrder);

        // 3. Return order
        return savedOrder;
    }

}
