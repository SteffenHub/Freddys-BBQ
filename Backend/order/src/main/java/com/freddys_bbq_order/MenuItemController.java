package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for managing menu items.
 * This API allows creating and retrieving menu items.
 */
@Tag(name = "Menu", description = "Menu Resource")
@RestController
@RequestMapping("/api/order/menu")
public class MenuItemController {

    private final MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Adds a new menu item.
     *
     * @param menuItem The menu item to be saved.
     * @return The saved menu item.
     */
    @Operation(summary = "Add a new menu item", description = "Save the given menu item to the Database")
    @PostMapping
    public MenuItem post(@RequestBody MenuItem menuItem) {
        return this.menuItemRepository.save(menuItem);
    }

    /**
     * Retrieves all menu items, optionally filtered by category.
     *
     * @param category (Optional) The category of the menu items(Main Course, Side, Drink).
     * @return A list of filtered or all menu items.
     */
    @Operation(
            summary = "Get menu items",
            description = """
        Returns menu items. You can optionally filter by category or retrieve a specific item by ID.
        - If no parameters are provided, all items are returned. (ResponseEntity<Iterable<MenuItem>>)
        - If `category` is provided, items matching that category will be returned.(ResponseEntity<List<MenuItem>>)
        - If `id` is provided, the specific menu item will be returned.(ResponseEntity<MenuItem>)
        - You may not provide both `category` and `id` in the same request.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items or single item"),
            @ApiResponse(responseCode = "400", description = "Invalid request – category and id cannot be combined"),
            @ApiResponse(responseCode = "404", description = "Item not found (when id is provided)")
    })
    @GetMapping
    public ResponseEntity<?> getAllItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID id) {
        // TODO test the changes for this function
        if (category != null && id != null) {
            return ResponseEntity.badRequest().build();
        }
        if (category != null) {
            return ResponseEntity.ok(menuItemRepository.findByCategory(category));
        }
        if (id != null) {
            Optional<MenuItem> item = menuItemRepository.findById(id);
            return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(menuItemRepository.findAll());
    }

}
