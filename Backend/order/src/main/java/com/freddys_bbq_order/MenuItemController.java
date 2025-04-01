package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItemO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @Operation(summary = "Add a new menu item", description = "Save the given menu item to the Database")
    @PostMapping
    public MenuItemO post(@RequestBody MenuItemO menuItem) {
        return this.menuItemRepository.save(menuItem);
    }

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
        if (category != null && id != null) {
            return ResponseEntity.badRequest().build();
        }
        if (category != null) {
            return ResponseEntity.ok(menuItemRepository.findByCategory(category));
        }
        if (id != null) {
            Optional<MenuItemO> item = menuItemRepository.findById(id);
            return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(menuItemRepository.findAll());
    }

    @Operation(
            summary = "Validate a menu item ID",
            description = """
        Checks whether a menu item with the specified ID exists in the database.
        This endpoint is typically used to validate IDs stored on the client side (e.g., in a cart).
        
        - If the ID exists, `true` is returned.
        - If the ID does not exist, `false` is returned.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation result returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format"),
    })
    @GetMapping("/validate-id")
    public ResponseEntity<Boolean> validateId(@RequestParam("id") UUID id) {
        Optional<MenuItemO> item = menuItemRepository.findById(id);
        return ResponseEntity.ok(item.isPresent());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidUUID(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Invalid UUID format: " + ex.getValue());
    }
}
