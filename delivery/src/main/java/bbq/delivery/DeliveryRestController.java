package bbq.delivery;

import bbq.delivery.model.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
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

}
