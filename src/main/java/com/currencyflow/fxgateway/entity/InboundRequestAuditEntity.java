package com.currencyflow.fxgateway.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "inbound_request_audit", indexes = {
        @Index(name = "idx_inbound_request_unique", columnList = "requestId", unique = true)
})
public class InboundRequestAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String requestId;

    @Column(nullable = false)
    private String externalServiceName;

    @Column(nullable = false)
    private Instant requestTimestampUtc;

    @Column(nullable = false)
    private String endClientId;

    @Column(nullable = false)
    private String requestType;

    @Column(nullable = false)
    private String currencyCode;

    public InboundRequestAuditEntity() {}
    public InboundRequestAuditEntity(String requestId, String externalServiceName, Instant requestTimestampUtc, String endClientId, String requestType, String currencyCode) {
        this.requestId = requestId;
        this.externalServiceName = externalServiceName;
        this.requestTimestampUtc = requestTimestampUtc;
        this.endClientId = endClientId;
        this.requestType = requestType;
        this.currencyCode = currencyCode;
    }

    public Long getId() { return id; }
}
