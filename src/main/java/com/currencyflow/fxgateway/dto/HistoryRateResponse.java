package com.currencyflow.fxgateway.dto;

import java.util.List;

public class HistoryRateResponse {
    private String requestId;
    private String client;
    private String currency;
    private Integer periodHours;
    private List<HistoryRateItem> items;

    public HistoryRateResponse(String requestId, String client, String currency, Integer periodHours, List<HistoryRateItem> items) {
        this.requestId = requestId;
        this.client = client;
        this.currency = currency;
        this.periodHours = periodHours;
        this.items = items;
    }

    public String getRequestId() { return requestId; }
    public String getClient() { return client; }
    public String getCurrency() { return currency; }
    public Integer getPeriodHours() { return periodHours; }
    public List<HistoryRateItem> getItems() { return items; }
}
