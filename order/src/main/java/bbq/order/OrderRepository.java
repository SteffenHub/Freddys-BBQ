package bbq.order;

import bbq.order.model.Order;
import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderRepository {

    private List<Order> orders = new ArrayList<>();

    private final HumanReadableIdGenerator idGenerator = new PermutationBasedHumanReadableIdGenerator();

    List<Order> findAll() {
        return orders;
    }

    Order save(Order order) {
        // 1. Assign id
        order.setId(idGenerator.generate());

        // 2. Save
        orders.add(order);

        // 3. Return saved Order
        return order;
    }

}
