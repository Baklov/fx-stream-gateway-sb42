package com.currencyflow.fxgateway.api;

import com.currencyflow.fxgateway.exception.DuplicateRequestException;
import com.currencyflow.fxgateway.exception.GatewayBadRequestException;
import com.currencyflow.fxgateway.exception.ResourceMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateRequestException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler(ResourceMissingException.class)
    public ResponseEntity<ErrorResponse> handleMissing(ResourceMissingException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler({GatewayBadRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "INTERNAL_SERVER_ERROR", ex.getMessage()));
    }
}
