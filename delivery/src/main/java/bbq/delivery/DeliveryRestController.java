package bbq.delivery;

import bbq.delivery.model.Delivery;
import bbq.delivery.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryRestController {

    private final DeliveryRepository deliveryRepository;

    @GetMapping
    public List<Delivery> get() {
        return deliveryRepository.getAll();
    }

    @GetMapping("/{orderId}")
    public Delivery getByOrderId(@PathVariable String orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Delivery post(@RequestBody Order order) {
        log.info("receive order: {}", order);
        return deliveryRepository.addNewOrder(order);
    }

}
