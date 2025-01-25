package bbq.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddress {

    private String name;
    private String city;
    private String street;
    private String zip;

}
