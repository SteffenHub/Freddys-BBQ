package bbq.order;

import bbq.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order post(@RequestBody Order order) {
        // 1. Save Order
        var savedOrder = orderRepository.save(order);

        // 2. Publish order
        rabbitTemplate.convertAndSend("orders", "", order);

        // 3. Return order
        return savedOrder;
    }
}
