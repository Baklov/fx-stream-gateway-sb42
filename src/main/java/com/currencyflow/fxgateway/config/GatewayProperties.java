package com.currencyflow.fxgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
    private final Rates rates = new Rates();
    private final Duplicate duplicate = new Duplicate();
    private final Kafka kafka = new Kafka();

    public Rates getRates() { return rates; }
    public Duplicate getDuplicate() { return duplicate; }
    public Kafka getKafka() { return kafka; }

    public static class Rates {
        private long pollIntervalMs = 60000;
        private String baseCurrency = "EUR";
        private List<String> trackedCurrencies = new ArrayList<>();
        public long getPollIntervalMs() { return pollIntervalMs; }
        public void setPollIntervalMs(long pollIntervalMs) { this.pollIntervalMs = pollIntervalMs; }
        public String getBaseCurrency() { return baseCurrency; }
        public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
        public List<String> getTrackedCurrencies() { return trackedCurrencies; }
        public void setTrackedCurrencies(List<String> trackedCurrencies) { this.trackedCurrencies = trackedCurrencies; }
    }

    public static class Duplicate {
        private long ttlHours = 24;
        public long getTtlHours() { return ttlHours; }
        public void setTtlHours(long ttlHours) { this.ttlHours = ttlHours; }
    }

    public static class Kafka {
        private String requestTopic = "fx.gateway.request.topic";
        private String rateTopic = "fx.gateway.rate.topic";
        public String getRequestTopic() { return requestTopic; }
        public void setRequestTopic(String requestTopic) { this.requestTopic = requestTopic; }
        public String getRateTopic() { return rateTopic; }
        public void setRateTopic(String rateTopic) { this.rateTopic = rateTopic; }
    }
}
