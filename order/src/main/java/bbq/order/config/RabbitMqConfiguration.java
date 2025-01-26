package bbq.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RabbitMqConfiguration {

    /**
     @Bean public Jackson2JsonMessageConverter messageConverter() {
     var messageConverter = new Jackson2JsonMessageConverter();
     messageConverter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
     return messageConverter;
     }

     @Bean public Declarables rabbitDeclarables() {
     return new Declarables();
     }
     */

}
