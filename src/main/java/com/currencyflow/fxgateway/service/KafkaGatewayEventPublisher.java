package com.currencyflow.fxgateway.service;

import com.currencyflow.fxgateway.config.GatewayProperties;
import com.currencyflow.fxgateway.entity.CurrencySnapshotEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KafkaGatewayEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GatewayProperties properties;

    public KafkaGatewayEventPublisher(KafkaTemplate<String, String> kafkaTemplate, GatewayProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    public void publishRequestEvent(String serviceName, String requestId, Instant requestTime, String clientId, String requestType, String currencyCode) {
        String payload = "{"
                + "\"serviceName\":\"" + serviceName + "\","
                + "\"requestId\":\"" + requestId + "\","
                + "\"requestTimeUtc\":\"" + requestTime + "\","
                + "\"clientId\":\"" + clientId + "\","
                + "\"requestType\":\"" + requestType + "\","
                + "\"currencyCode\":\"" + currencyCode + "\""
                + "}";
        kafkaTemplate.send(properties.getKafka().getRequestTopic(), requestId, payload);
    }

    public void publishRateEvent(CurrencySnapshotEntity snapshot) {
        String payload = "{"
                + "\"currencyCode\":\"" + snapshot.getCurrencyCode() + "\","
                + "\"baseCurrency\":\"" + snapshot.getBaseCurrency() + "\","
                + "\"rateValue\":\"" + snapshot.getRateValue() + "\","
                + "\"snapshotTime\":\"" + snapshot.getSnapshotTime() + "\""
                + "}";
        kafkaTemplate.send(properties.getKafka().getRateTopic(), snapshot.getCurrencyCode(), payload);
    }
}
