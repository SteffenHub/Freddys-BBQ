package linkedinlearning.springbootapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("www/greeting")
public class GreetingWebController {
    
    @GetMapping    
    public String index(Model model) {
        model.addAttribute("message", "Hello there!");
        return "greeting";
    }
}
