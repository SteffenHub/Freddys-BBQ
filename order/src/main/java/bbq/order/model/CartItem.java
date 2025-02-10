package bbq.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String itemId;
    private String itemTitle;
    private Integer count;
    private BigDecimal itemPrice;
    private BigDecimal total;

}
