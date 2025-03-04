package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/order/menu")
public class MenuControllerBackend {

  private final MenuItemRepository menuItemRepository;

  public MenuControllerBackend(MenuItemRepository menuItemRepository) {
    this.menuItemRepository = menuItemRepository;
  }

  @GetMapping
  public List<MenuItem> index(){
    return (List<MenuItem>) menuItemRepository.findAll();
  }
}
