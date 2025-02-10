package bbq.order.config;


import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return messageConverter;
    }

    @Bean
    public Declarables rabbitDeclarables() {
        // Publish/Subscribe
        // 1. Exchange
        var ordersExchange = new FanoutExchange("orders");

        // a) delivery orders
        // 2. Queue
        var deliveryOrdersQueue = QueueBuilder.nonDurable("delivery.orders").build();
        // 3. Binding
        var deliveryOrdersBinding = BindingBuilder.bind(deliveryOrdersQueue).to(ordersExchange);

        // b) kitchen orders
        // 2. Queue
        // TODO
        // 3. Binding
        // TODO

        return new Declarables(
                ordersExchange,
                deliveryOrdersQueue,
                deliveryOrdersBinding
        );
    }
}
