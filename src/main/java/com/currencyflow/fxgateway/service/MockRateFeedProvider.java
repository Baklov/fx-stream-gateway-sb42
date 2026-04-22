package com.currencyflow.fxgateway.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@org.springframework.context.annotation.Profile("mock-rates")
public class MockRateFeedProvider implements RateFeedProvider {
    @Override
    public Map<String, BigDecimal> fetchLatestRates(String baseCurrency) {
        Map<String, BigDecimal> rates = new LinkedHashMap<>();
        rates.put("EUR", value(1.00000000));
        rates.put("USD", value(1.07 + randomDelta()));
        rates.put("GBP", value(0.85 + randomDelta()));
        rates.put("CHF", value(0.97 + randomDelta()));
        rates.put("BGN", value(1.95583));
        return rates;
    }

    private double randomDelta() {
        return ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
    }

    private BigDecimal value(double number) {
        return BigDecimal.valueOf(number).setScale(8, RoundingMode.HALF_UP);
    }
}
