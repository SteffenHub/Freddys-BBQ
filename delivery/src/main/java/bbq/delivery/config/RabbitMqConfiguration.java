package bbq.delivery.config;


import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
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
        // Topic
        // 1. Exchange
        var updatesExchange = new TopicExchange("delivery.updates");
        // 2. Queue
        //  a) delivered
        var deliveredQueue = QueueBuilder.durable("orders.delivered").build();
        //  a) inprogress
        var inProgressQueue = QueueBuilder.durable("orders.inprogress").build();
        // 3. Bindings
        var deliveredBinding = BindingBuilder.bind(deliveredQueue)
                .to(updatesExchange)
                .with("delivered");
        var inProgressBinding = BindingBuilder.bind(inProgressQueue)
                .to(updatesExchange)
                .with("inprogress");

        return new Declarables(
                updatesExchange,
                deliveredQueue,
                inProgressQueue,
                deliveredBinding,
                inProgressBinding
        );
    }
}
