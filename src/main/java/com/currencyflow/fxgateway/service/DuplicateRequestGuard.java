package com.currencyflow.fxgateway.service;

import com.currencyflow.fxgateway.config.GatewayProperties;
import com.currencyflow.fxgateway.exception.DuplicateRequestException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DuplicateRequestGuard {
    private final StringRedisTemplate redisTemplate;
    private final GatewayProperties properties;

    public DuplicateRequestGuard(StringRedisTemplate redisTemplate, GatewayProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    public void ensureNewRequest(String requestId) {
        String key = "fx:request:" + requestId;
        Boolean stored = redisTemplate.opsForValue().setIfAbsent(
                key, "1", Duration.ofHours(properties.getDuplicate().getTtlHours())
        );
        if (Boolean.FALSE.equals(stored)) {
            throw new DuplicateRequestException(requestId);
        }
    }
}
