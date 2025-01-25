package bbq.order;

import bbq.order.model.MenuCategory;
import bbq.order.model.MenuItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuRestController {

    private final MenuRepository menuRepository;

    @GetMapping
    public List<MenuCategory> get() {
        return menuRepository.findAll();
    }

    @GetMapping("/{key}/menu-items")
    public List<MenuItem> getByKey(@PathVariable String key, @RequestParam Optional<String> sort) {
        return menuRepository.findItemsByCategoryKey(key, sort);
    }

    @GetMapping("/response-entity")
    public ResponseEntity<List<MenuCategory>> getResponseEntity() {
        var menuCategories = menuRepository.findAll();
        if (menuCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(menuCategories);
    }

}
