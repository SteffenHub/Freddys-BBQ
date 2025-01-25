package bbq.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private LocalDateTime receivedAt;

    private LocalDateTime deliveredAt;

    @Builder.Default
    private String status = "-unknown-";

    private Order order;

    @Builder.Default
    private int progress = 0;

    public void nextStatus() {
        var now = LocalDateTime.now();
        var ageMinutes = ChronoUnit.MINUTES.between(getReceivedAt(), now);

        switch (status) {
            case "Received":
                setStatus("In Preparation");
                setProgress(50);
                break;
            case "In Preparation":
                setStatus("In Delivery");
                setProgress(80);
                break;
            case "In Delivery":
                setStatus("Delivered");
                setProgress(100);
                setDeliveredAt(now);
                break;
        }
    }
}
