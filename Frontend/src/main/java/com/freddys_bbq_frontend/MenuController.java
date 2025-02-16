package com.freddys_bbq_frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/")
public class MenuController {

  private final MenuItemRepository menuItemRepository;

  public MenuController(MenuItemRepository menuItemRepository) {
    this.menuItemRepository = menuItemRepository;
  }

  @GetMapping
  public String index(Model model){
    Iterable<MenuItem> menu = menuItemRepository.findByOrderByDrinkDescNameDesc();
    model.addAttribute("menuItems", menu);
    return "index";
  }
}
