package com.currencyflow.fxgateway.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class CurrentRateResponse {
    private String requestId;
    private String client;
    private String currency;
    private String baseCurrency;
    private BigDecimal rate;
    private Instant rateTimestamp;

    public CurrentRateResponse(String requestId, String client, String currency, String baseCurrency, BigDecimal rate, Instant rateTimestamp) {
        this.requestId = requestId;
        this.client = client;
        this.currency = currency;
        this.baseCurrency = baseCurrency;
        this.rate = rate;
        this.rateTimestamp = rateTimestamp;
    }

    public String getRequestId() { return requestId; }
    public String getClient() { return client; }
    public String getCurrency() { return currency; }
    public String getBaseCurrency() { return baseCurrency; }
    public BigDecimal getRate() { return rate; }
    public Instant getRateTimestamp() { return rateTimestamp; }
}
