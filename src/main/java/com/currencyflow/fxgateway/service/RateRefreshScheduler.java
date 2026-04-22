package com.currencyflow.fxgateway.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateRefreshScheduler {
    private static final Logger log = LoggerFactory.getLogger(RateRefreshScheduler.class);

    private final CurrencyQueryService currencyQueryService;

    public RateRefreshScheduler(CurrencyQueryService currencyQueryService) {
        this.currencyQueryService = currencyQueryService;
    }

   @Scheduled(
            initialDelayString = "${gateway.rates.poll-interval-ms}",
            fixedDelayString = "${gateway.rates.poll-interval-ms}"
    )
    public void scheduledRefresh() {
       try {
           currencyQueryService.refreshRates();
       } catch (Exception e) {
           log.error("Rate refresh failed", e);
       }
    }
}