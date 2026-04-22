package com.currencyflow.fxgateway.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "currency_snapshot", indexes = {
        @Index(name = "idx_currency_snapshot_currency_time", columnList = "currencyCode, snapshotTime")
})
public class CurrencySnapshotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private String baseCurrency;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal rateValue;

    @Column(nullable = false)
    private Instant snapshotTime;

    public CurrencySnapshotEntity() {}
    public CurrencySnapshotEntity(String currencyCode, String baseCurrency, BigDecimal rateValue, Instant snapshotTime) {
        this.currencyCode = currencyCode;
        this.baseCurrency = baseCurrency;
        this.rateValue = rateValue;
        this.snapshotTime = snapshotTime;
    }

    public Long getId() { return id; }
    public String getCurrencyCode() { return currencyCode; }
    public String getBaseCurrency() { return baseCurrency; }
    public BigDecimal getRateValue() { return rateValue; }
    public Instant getSnapshotTime() { return snapshotTime; }
}
