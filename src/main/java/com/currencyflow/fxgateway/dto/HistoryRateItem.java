package com.currencyflow.fxgateway.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class HistoryRateItem {
    private String currency;
    private String baseCurrency;
    private BigDecimal rate;
    private Instant timestamp;

    public HistoryRateItem(String currency, String baseCurrency, BigDecimal rate, Instant timestamp) {
        this.currency = currency;
        this.baseCurrency = baseCurrency;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public String getCurrency() { return currency; }
    public String getBaseCurrency() { return baseCurrency; }
    public BigDecimal getRate() { return rate; }
    public Instant getTimestamp() { return timestamp; }
}
