package com.freddys_bbq_order.config;

import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitConfiguration {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(final Queue queue, final Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        final Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.freddys_bbq_order");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
