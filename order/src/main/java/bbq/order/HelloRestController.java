package bbq.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello", description = "Hello Resource")
public class HelloRestController {

    @GetMapping
    public String get() {
        return "Hello Order!";
    }

}
