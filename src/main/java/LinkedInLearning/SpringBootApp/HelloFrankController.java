package LinkedInLearning.SpringBootApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloFrankController {

    @Autowired
    private GrettingService service;

    // Konstruktor injection
    // public HelloFrankController(HelloFrankService service) {
    //    this.service = service;
    // }
    
    @GetMapping("/")
    public String get() {
        return this.service.hello();
    }
}
