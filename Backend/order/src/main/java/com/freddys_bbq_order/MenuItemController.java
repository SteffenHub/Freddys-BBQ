package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/order/menu")
public class MenuItemController {

  @Autowired
  private MenuItemRepository menuItemRepository;

  @PostMapping
  public MenuItem post(@RequestBody MenuItem menuItem) {
    return this.menuItemRepository.save(menuItem);
  }

  @GetMapping
  public Iterable<MenuItem> getAllItems(@RequestParam(required = false) String category) {
      if (category != null) {
          return menuItemRepository.findByCategory(category);
      }
      return menuItemRepository.findAll();
  }

}
