package linkedin.bbq_joint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/")
public class MenuController {
  
  @Autowired
  private MenuItemRepository menuItemRepository;

  @GetMapping
  public String index(Model model){
    Iterable<MenuItem> menu = menuItemRepository.findByOrderByDrinkDescNameDesc();
    model.addAttribute("menuItems", menu);
    return "index";
  }
  

}
