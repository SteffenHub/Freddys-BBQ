package bbq.order;

import bbq.order.model.Order;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Tag(name = "Order", description = "Order Resource")
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final OrderRestClientPublisher publisher;

    @GetMapping("/crash")
    public String crash() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found nothing...");
        //throw new IllegalArgumentException("Don't get it.");
//        throw new RuntimeException("Bam!");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order post(@Valid @RequestBody Order order) {
        // Save Order
        //return orderRepository.save(order);
        var savedOrder = orderRepository.save(order);

        // 2. Publish order
        publisher.publish(savedOrder);

        return savedOrder;
    }

}
