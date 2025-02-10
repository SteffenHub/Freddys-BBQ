package bbq.delivery.config;

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
}
