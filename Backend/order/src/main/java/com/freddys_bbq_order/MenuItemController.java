package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

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
    @Operation(summary = "Get all menu items", description = "Gives back a list of all menu items. Optionally, can be filtered by category")
    @GetMapping
    public Iterable<MenuItem> getAllItems(@RequestParam(required = false) String category) {
        if (category != null) {
            return menuItemRepository.findByCategory(category);
        }
        return menuItemRepository.findAll();
    }

}
