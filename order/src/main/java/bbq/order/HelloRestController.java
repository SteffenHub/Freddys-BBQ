package bbq.order;

import bbq.order.model.Cart;
import bbq.order.model.CartItem;
import bbq.order.model.Order;
import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class HelloRestController {

    private final RabbitTemplate rabbitTemplate;

    private final HumanReadableIdGenerator idGenerator = new PermutationBasedHumanReadableIdGenerator();

    @GetMapping
    public String get() {
        return "Hello Order!";
    }

    @GetMapping("/send")
    public String send() {
        var order = new Order();
        order.setId(idGenerator.generate());
        var cart = new Cart();
        cart.setTotal(BigDecimal.TEN);
        var cartItem = new CartItem();
        cartItem.setItemTitle("Burger");
        cartItem.setItemPrice(BigDecimal.TEN);
        cart.setItems(List.of(cartItem));
        order.setCart(cart);
        rabbitTemplate.convertAndSend("orders", "",  order);
        return "Done";
    }

}
