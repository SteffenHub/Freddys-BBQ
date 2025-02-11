package bbq.order.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;

    @Valid
    private Cart cart;

    @Valid
    private BillingAddress billingAddress;

}
