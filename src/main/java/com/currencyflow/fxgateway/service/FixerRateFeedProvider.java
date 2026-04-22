package com.currencyflow.fxgateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.currencyflow.fxgateway.config.FixerProperties;
import com.currencyflow.fxgateway.dto.FixerLatestResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FixerRateFeedProvider implements RateFeedProvider {

    private static final Logger log = LoggerFactory.getLogger(FixerRateFeedProvider.class);

    private final WebClient webClient;
    private final FixerProperties fixerProperties;

    public FixerRateFeedProvider(FixerProperties fixerProperties) {
        this.fixerProperties = fixerProperties;
        this.webClient = WebClient.builder()
                .baseUrl(fixerProperties.getUrl())
                .build();
    }

    @Override
    public Map<String, BigDecimal> fetchLatestRates(String baseCurrency) {
        log.info("Calling fixer.io with baseCurrency={}", baseCurrency);

        if (!StringUtils.hasText(fixerProperties.getApiKey())) {
            throw new IllegalStateException("Missing FIXER_API_KEY. Set it as environment variable.");
        }

        FixerLatestResponse response = webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/latest")
                            .queryParam("access_key", fixerProperties.getApiKey());

                    if (StringUtils.hasText(baseCurrency)) {
                        builder.queryParam("base", baseCurrency);
                    }

                    return builder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Fixer HTTP error: " + clientResponse.statusCode() + ", body=" + body
                                )))
                )
                .bodyToMono(FixerLatestResponse.class)
                .timeout(Duration.ofMillis(fixerProperties.getTimeoutMs()))
                .block();

        if (response == null) {
            throw new IllegalStateException("No response from fixer.io");
        }

        if (!response.isSuccess()) {
            String message = response.getError() != null
                    ? response.getError().getInfo()
                    : "Unknown Fixer API error";
            throw new IllegalStateException("Fixer API error: " + message);
        }

        if (response.getRates() == null || response.getRates().isEmpty()) {
            throw new IllegalStateException("Fixer API returned empty rates");
        }

        Map<String, BigDecimal> normalizedRates = new LinkedHashMap<>(response.getRates());

        String effectiveBase = StringUtils.hasText(response.getBase()) ? response.getBase() : baseCurrency;
        if (StringUtils.hasText(effectiveBase) && !normalizedRates.containsKey(effectiveBase)) {
            normalizedRates.put(effectiveBase, BigDecimal.ONE);
        }

        return normalizedRates;
    }
}