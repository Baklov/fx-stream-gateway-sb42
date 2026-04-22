package com.currencyflow.fxgateway.service;

import com.currencyflow.fxgateway.entity.InboundRequestAuditEntity;
import com.currencyflow.fxgateway.repository.InboundRequestAuditRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InboundAuditService {
    private final InboundRequestAuditRepository repository;
    private final KafkaGatewayEventPublisher eventPublisher;

    public InboundAuditService(InboundRequestAuditRepository repository, KafkaGatewayEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public void storeAudit(String requestId, String serviceName, Instant timestampUtc, String endClientId, String requestType, String currencyCode) {
        InboundRequestAuditEntity row = new InboundRequestAuditEntity(requestId, serviceName, timestampUtc, endClientId, requestType, currencyCode);
        repository.save(row);
        eventPublisher.publishRequestEvent(serviceName, requestId, timestampUtc, endClientId, requestType, currencyCode);
    }
}
