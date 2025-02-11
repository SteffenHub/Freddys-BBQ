package bbq.order;

import bbq.order.model.MenuCategory;
import bbq.order.model.MenuItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MenuRepository {

    private final List<MenuCategory> menuCategories = List.of(
            MenuCategory.builder()
                    .id(UUID.randomUUID())
                    .key("main-course")
                    .title("Main Courses")
                    .items(
                            List.of(
                                    MenuItem.builder()
                                            .id(UUID.fromString("0da7f6f4-a472-4ff7-89e2-0e416942614c"))
                                            .price(new BigDecimal("21.9"))
                                            .title("Freddy's Rib Special")
                                            .imageUrl("https://unsplash.com/photos/0hOHNA3M6Ds/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8OHx8cmlic3xlbnwwfHx8fDE3MTQyOTU0MjN8MA&force=true&w=640")
                                            .build(),
                                    MenuItem.builder()
                                            .id(UUID.fromString("6212c1e0-236a-4b74-91e6-4ccecb64e693"))
                                            .price(new BigDecimal("16.5"))
                                            .title("BBQ Burger and Fries")
                                            .imageUrl("https://unsplash.com/photos/uVPV_nV17Tw/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNzE0MzMzMzA4fA&force=true&w=640")
                                            .build(),
                                    MenuItem.builder()
                                            .id(UUID.fromString("5436f685-4dd0-4ba5-b7a7-55cca56f34c7"))
                                            .price(new BigDecimal("10.50"))
                                            .imageUrl("https://unsplash.com/photos/4qzaeR_sTYA/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8bWFjJTIwYW5kJTIwY2hlZXNlfGVufDB8fHx8MTcxNDMzMzE4MHwy&force=true&w=640")
                                            .title("Mac and Cheese")
                                            .build()
                            )
                    )
                    .build(),
            MenuCategory.builder()
                    .id(UUID.randomUUID())
                    .key("sides")
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
                    .key("drinks")
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

    List<MenuCategory> findAll() {
        return menuCategories;
    }

    MenuItem findItemById(UUID id) {
        for (MenuCategory menuCategory : menuCategories) {
            for (MenuItem menuItem : menuCategory.getItems()) {
                if (menuItem.getId().equals(id)) {
                    return menuItem;
                }
            }
        }
        return null;
    }

    void deleteMenuItemById(UUID id) {
        for (MenuCategory menuCategory : menuCategories) {
            menuCategory.setItems(
                    menuCategory.getItems().stream()
                            .filter(menuItem -> !menuItem.getId().equals(id))
                            .toList()
            );
        }
    }

    List<MenuItem> findItemsByCategoryKey(String key, Optional<String> sort) {
        var menuCategory = menuCategories.stream()
                .filter(c -> c.getKey().equals(key))
                .findFirst();
        if (menuCategory.isEmpty()) {
            return Collections.emptyList();
        }

        if (sort.isEmpty()) {
            return menuCategory.get().getItems();
        }

        // Sort items
        var sortBy = sort.get().toLowerCase();
        var items = menuCategory.orElseThrow().getItems();
        return items.stream().sorted((o1, o2) -> {
            if ("price".equals(sortBy)) {
                return o1.getPrice().compareTo(o2.getPrice());
            } else if ("title".equals(sortBy)) {
                return o1.getTitle().compareTo(o2.getTitle());
            } else {
                throw new IllegalArgumentException("Unknown sort key: " + sortBy);
            }
        }).toList();
    }

}
