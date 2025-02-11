package bbq.delivery.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic deliveryUpdatesTopic() {
        return TopicBuilder.name("delivery_updates").build();
    }
}
