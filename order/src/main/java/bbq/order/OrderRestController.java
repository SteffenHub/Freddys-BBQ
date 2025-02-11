package bbq.order;

import bbq.order.model.Order;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Order", description = "Order Resource")
@RequestMapping("/api/order")
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final OrderRestTemplatePublisher publisher;

    private final Counter orderCounter;

    public OrderRestController(
            OrderRepository orderRepository,
            OrderRestTemplatePublisher publisher,
            MeterRegistry registry) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
        this.orderCounter = Counter.builder("orders.placed")
                .description("Number of orders placed")
                .register(registry);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order post(
            JwtAuthenticationToken principal,
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestBody Order order
    ) {
        try {
            // 1. Save Order
            var savedOrder = orderRepository.save(order);

            // 2. Publish order
            publisher.publish(savedOrder);

            return savedOrder;
        } finally {
            orderCounter.increment();
        }
    }

}
