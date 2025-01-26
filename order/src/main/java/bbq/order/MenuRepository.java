package bbq.order;

import bbq.order.model.MenuCategory;
import bbq.order.model.MenuItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class MenuRepository {

    List<MenuCategory> get() {
        return List.of(
                MenuCategory.builder()
                        .id(UUID.randomUUID())
                        .title("Main Courses")
                        .items(
                                List.of(
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .price(new BigDecimal("21.9"))
                                                .title("Freddy's Rib Special")
                                                .imageUrl("https://unsplash.com/photos/0hOHNA3M6Ds/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8OHx8cmlic3xlbnwwfHx8fDE3MTQyOTU0MjN8MA&force=true&w=640")
                                                .build(),
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .price(new BigDecimal("16.5"))
                                                .title("BBQ Burger and Fries")
                                                .imageUrl("https://unsplash.com/photos/uVPV_nV17Tw/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNzE0MzMzMzA4fA&force=true&w=640")
                                                .build(),
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .price(new BigDecimal("10.50"))
                                                .imageUrl("https://unsplash.com/photos/4qzaeR_sTYA/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8bWFjJTIwYW5kJTIwY2hlZXNlfGVufDB8fHx8MTcxNDMzMzE4MHwy&force=true&w=640")
                                                .title("Mac and Cheese")
                                                .build()
                                )
                        )
                        .build(),
                MenuCategory.builder()
                        .id(UUID.randomUUID())
                        .title("Sides")
                        .items(
                                List.of(
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .title("Coleslaw Salad")
                                                .imageUrl("https://unsplash.com/photos/btS7sL3jprM/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8N3x8Y29sZXNsYXclMjBzYWxhZHxlbnwwfHx8fDE3MTQzMzMzNDF8Mg&force=true&w=640")
                                                .price(new BigDecimal("4.8"))
                                                .build(),
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .price(new BigDecimal("6.8"))
                                                .imageUrl("https://unsplash.com/photos/zEBe2beserI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8bWFzaGVkJTIwcG90YXRvZXN8ZW58MHx8fHwxNzE0MzMzNDEyfDI&force=true&w=640")
                                                .title("Sweet Potatoe Mash")
                                                .build()
                                )
                        )
                        .build(),

                MenuCategory.builder()
                        .id(UUID.randomUUID())
                        .title("Drinks")
                        .items(
                                List.of(
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .title("Lemonade")
                                                .price(new BigDecimal("3.5"))
                                                .imageUrl("https://unsplash.com/photos/sSLqRCTJBvU/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8M3x8bGVtb25hZGV8ZW58MHx8fHwxNzE0MzMzNDYyfDI&force=true&w=640")
                                                .build(),
                                        MenuItem.builder()
                                                .id(UUID.randomUUID())
                                                .price(new BigDecimal("4.4"))
                                                .imageUrl("https://unsplash.com/photos/NfjfNQV47OU/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTd8fGJlZXJ8ZW58MHx8fHwxNzE0MjUxMjgwfDI&force=true&w=640")
                                                .title("Beer")
                                                .build()
                                )
                        )
                        .build()
        );
    }

}
