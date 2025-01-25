package bbq.order;

import bbq.order.model.MenuCategory;
import bbq.order.model.MenuItem;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Menu", description = "Menu Resource")
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuRestController {

    private final MenuRepository menuRepository;

    @Operation(summary = "Get menu", description = "The full menu with all categories and items.")
    @GetMapping
    public List<MenuCategory> get() {
        return menuRepository.findAll();
    }

    @Operation(summary = "Get Items by Category", description = "All menu items in a specific category.")
    @ApiResponse(
            responseCode = "500",
            description = "Something went seriously wrong.",
            content = {@Content(schema = @Schema(implementation = ProblemDetail.class))}
    )
    @GetMapping("/{key}/menu-items")
    public List<MenuItem> getByKey(@PathVariable String key, @RequestParam Optional<String> sort) {
        return menuRepository.findItemsByCategoryKey(key, sort);
    }

    // TODO: Patch
    @PatchMapping("/menu-items/{key}")
    public MenuItem update(@PathVariable UUID key, @RequestBody MenuItem menuItemPatch) {

        MenuItem menuItem = this.menuRepository.findItemById(key);

        if (menuItemPatch.getPrice() != null) {
            menuItem.setPrice(menuItemPatch.getPrice());
        }
        if (menuItemPatch.getTitle() != null) {
            menuItem.setTitle(menuItemPatch.getTitle());
        }
        if (menuItemPatch.getDescription() != null) {
            menuItem.setDescription(menuItemPatch.getDescription());
        }
        if (menuItemPatch.getImageUrl() != null) {
            menuItem.setImageUrl(menuItemPatch.getImageUrl());
        }

        return menuItem;
    }

    // TODO: Delete

    @Hidden
    @GetMapping("/response-entity")
    public ResponseEntity<List<MenuCategory>> getResponseEntity() {
        var menuCategories = menuRepository.findAll();
        if (menuCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(menuCategories);
    }

}
