package bbq.order.config;

import bbq.order.model.Cart;
import bbq.order.model.Order;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class DummyOrderProducerRunner implements ApplicationRunner {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Sending dummy order for queue setup");
        var order = new Order();
        order.setId(new PermutationBasedHumanReadableIdGenerator().generate());
        var cart = new Cart();
        cart.setTotal(BigDecimal.TEN);
        cart.setItems(Collections.emptyList());
        order.setCart(cart);
        rabbitTemplate.convertAndSend("orders", "",  order);
    }
}
