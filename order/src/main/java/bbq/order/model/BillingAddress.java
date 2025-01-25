package bbq.order.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddress {

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    @NotEmpty
    private String street;

    @NotEmpty
    private String zip;

}
