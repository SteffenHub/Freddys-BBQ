package bbq.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;

    private Cart cart;

    private BillingAddress billingAddress;

}
