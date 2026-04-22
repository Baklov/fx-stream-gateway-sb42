package com.currencyflow.fxgateway.exception;

public class DuplicateRequestException extends RuntimeException {
    public DuplicateRequestException(String requestId) {
        super("Duplicate request detected for id: " + requestId);
    }
}
