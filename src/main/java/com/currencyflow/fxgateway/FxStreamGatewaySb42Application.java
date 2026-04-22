package com.currencyflow.fxgateway;

import com.currencyflow.fxgateway.config.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(GatewayProperties.class)
public class FxStreamGatewaySb42Application {

    public static void main(String[] args) {
        SpringApplication.run(FxStreamGatewaySb42Application.class, args);
    }

}
