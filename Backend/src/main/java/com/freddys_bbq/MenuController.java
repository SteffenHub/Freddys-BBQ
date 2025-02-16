package com.freddys_bbq;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/menu")
public class MenuController {

  private final MenuItemRepository menuItemRepository;

  public MenuController(MenuItemRepository menuItemRepository) {
    this.menuItemRepository = menuItemRepository;
  }

  @GetMapping
  public List<MenuItem> index(){
    return (List<MenuItem>) menuItemRepository.findByOrderByDrinkDescNameDesc();
  }
}
