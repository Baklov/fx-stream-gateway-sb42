package com.currencyflow.fxgateway.service;

import com.currencyflow.fxgateway.config.GatewayProperties;
import com.currencyflow.fxgateway.entity.CurrencySnapshotEntity;
import com.currencyflow.fxgateway.exception.ResourceMissingException;
import com.currencyflow.fxgateway.repository.CurrencySnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyQueryService {
    private static final Logger log = LoggerFactory.getLogger(CurrencyQueryService.class);

    private final CurrencySnapshotRepository repository;
    private final RateFeedProvider rateFeedProvider;
    private final GatewayProperties properties;
    private final KafkaGatewayEventPublisher eventPublisher;

    public CurrencyQueryService(CurrencySnapshotRepository repository, RateFeedProvider rateFeedProvider, GatewayProperties properties, KafkaGatewayEventPublisher eventPublisher) {
        this.repository = repository;
        this.rateFeedProvider = rateFeedProvider;
        this.properties = properties;
        this.eventPublisher = eventPublisher;
    }

    public void refreshRates() {
        log.info("Starting refreshRates()");

        String baseCurrency = properties.getRates().getBaseCurrency();
        Instant now = Instant.now();
        Map<java.lang.String, java.math.BigDecimal> rates = rateFeedProvider.fetchLatestRates(baseCurrency);

        List<String> tracked = properties.getRates().getTrackedCurrencies();

        List<CurrencySnapshotEntity> rows;
        if (tracked == null || tracked.isEmpty()) {
            rows = rates.entrySet().stream()
                    .map(entry -> new CurrencySnapshotEntity(entry.getKey(), baseCurrency, entry.getValue(), now))
                    .collect(Collectors.toList());
        } else {
            rows = tracked.stream()
                    .filter(rates::containsKey)
                    .map(code -> new CurrencySnapshotEntity(code, baseCurrency, rates.get(code), now))
                    .collect(Collectors.toList());
        }

        repository.saveAll(rows);
        rows.forEach(eventPublisher::publishRateEvent);
        log.info("Finishing refreshRates()");

    }

    public CurrencySnapshotEntity getLatest(String currencyCode) {
        return repository.findTopByCurrencyCodeOrderBySnapshotTimeDesc(currencyCode)
                .orElseThrow(() -> new ResourceMissingException("No latest rate found for currency: " + currencyCode));
    }

    public List<CurrencySnapshotEntity> getHistory(String currencyCode, int periodHours) {
        Instant threshold = Instant.now().minusSeconds(periodHours * 3600L);
        List<CurrencySnapshotEntity> items = repository.findByCurrencyCodeAndSnapshotTimeGreaterThanEqualOrderBySnapshotTimeDesc(currencyCode, threshold);
        if (items.isEmpty()) {
            throw new ResourceMissingException("No historical rates found for currency: " + currencyCode);
        }
        return items;
    }
}
