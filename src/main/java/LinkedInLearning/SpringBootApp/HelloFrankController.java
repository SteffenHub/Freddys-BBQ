package LinkedInLearning.SpringBootApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloFrankController {

    private HelloFrankService component;

    public HelloFrankController(HelloFrankService component) {
        this.component = component;
    }
    
    @GetMapping("/")
    public String get() {
        return this.component.hello();
    }
}
