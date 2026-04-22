package com.currencyflow.fxgateway.exception;

public class GatewayBadRequestException extends RuntimeException {
    public GatewayBadRequestException(String message) {
        super(message);
    }
}
