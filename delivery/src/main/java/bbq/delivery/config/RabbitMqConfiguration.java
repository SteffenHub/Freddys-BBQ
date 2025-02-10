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

    public Declarables rabbitDeclarables() {

        var updateExchange = new TopicExchange("delivery.updates");

        var deliveredQueue = QueueBuilder.durable("orders.delivered").build();
        var inProgressQueue = QueueBuilder.durable("orders.inprogress").build();

        var deliveredBinding = BindingBuilder.bind(deliveredQueue).to(updateExchange).with("delivered");

        var inprogressBinding = BindingBuilder.bind(inProgressQueue).to(updateExchange).with("inprogess");

        return new Declarables(
                updateExchange,
                deliveredQueue,
                inProgressQueue,
                deliveredBinding,
                inprogressBinding
        );
    }
}
