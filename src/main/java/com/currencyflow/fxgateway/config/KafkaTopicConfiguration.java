package com.currencyflow.fxgateway.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfiguration {
    @Bean
    public NewTopic requestTopic(GatewayProperties properties) {
        return new NewTopic(properties.getKafka().getRequestTopic(), 1, (short) 1);
    }

    @Bean
    public NewTopic rateTopic(GatewayProperties properties) {
        return new NewTopic(properties.getKafka().getRateTopic(), 1, (short) 1);
    }
}
