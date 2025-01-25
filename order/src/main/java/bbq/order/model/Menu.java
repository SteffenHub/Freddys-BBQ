package bbq.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Menu {
 List<MenuCategory> categories;
}
