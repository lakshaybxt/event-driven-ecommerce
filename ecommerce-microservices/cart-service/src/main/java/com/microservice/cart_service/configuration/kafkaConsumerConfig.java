package com.microservice.cart_service.configuration;

import com.microservice.cart_service.kafka.event.CartEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class kafkaConsumerConfig {

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CartEvent> kafkaListenerContainerFactory(
            ConsumerFactory <String, CartEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, CartEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
