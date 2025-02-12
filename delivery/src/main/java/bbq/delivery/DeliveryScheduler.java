package bbq.delivery;

import bbq.delivery.model.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryScheduler {

    private final DeliveryRepository deliveryRepository;

    @Scheduled(fixedRateString = "PT5S")
    public void scheduleFixedRateTask() {
        log.info("Sending delivery updates at {}", LocalDateTime.now());
        deliveryRepository.getAll().forEach(this::process);
    }

    private void process(Delivery delivery) {
        // 1. Advance status
        delivery.nextStatus();
    }

}
