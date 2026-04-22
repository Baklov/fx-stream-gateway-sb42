package com.currencyflow.fxgateway.dto.xml;

import java.math.BigDecimal;

public class XmlHistoryRatePayload {
    private String currency;
    private String baseCurrency;
    private BigDecimal rate;
    private String timestamp;

    public XmlHistoryRatePayload() {}

    public XmlHistoryRatePayload(
            String currency,
            String baseCurrency,
            BigDecimal rate,
            String timestamp
    ) {
        this.currency = currency;
        this.baseCurrency = baseCurrency;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public String getCurrency() { return currency; }
    public String getBaseCurrency() { return baseCurrency; }
    public BigDecimal getRate() { return rate; }
    public String getTimestamp() { return timestamp; }

    public void setCurrency(String currency) { this.currency = currency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}