package bbq.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategory {
    private UUID id;
    private String key;
    private String title;
    private List<MenuItem> items;
}
