package com.currencyflow.fxgateway.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDiagnosticConsumer {
    @KafkaListener(topics = "${gateway.kafka.request-topic}", groupId = "fx-stream-gateway-group")
    public void onRequestEvent(String payload) {
        System.out.println("REQUEST EVENT: " + payload);
    }

    @KafkaListener(topics = "${gateway.kafka.rate-topic}", groupId = "fx-stream-gateway-group")
    public void onRateEvent(String payload) {
        System.out.println("RATE EVENT: " + payload);
    }
}
