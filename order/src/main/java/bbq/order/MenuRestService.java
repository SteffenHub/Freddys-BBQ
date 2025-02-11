package bbq.order;

import bbq.order.model.MenuCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuRestService {

    private final MenuRepository menuRepository;

    @GetMapping
    public List<MenuCategory> get() {
        return menuRepository.get();
    }
}
