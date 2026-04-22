package com.currencyflow.fxgateway.service;

import java.math.BigDecimal;
import java.util.Map;

public interface RateFeedProvider {
    Map<String, BigDecimal> fetchLatestRates(String baseCurrency);
}
