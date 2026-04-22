package com.currencyflow.fxgateway.controller;

import com.currencyflow.fxgateway.dto.CurrentRateRequest;
import com.currencyflow.fxgateway.dto.CurrentRateResponse;
import com.currencyflow.fxgateway.dto.HistoryRateRequest;
import com.currencyflow.fxgateway.dto.HistoryRateResponse;
import com.currencyflow.fxgateway.service.GatewayFacadeService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/json_api", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonGatewayController {
    private final GatewayFacadeService gatewayFacadeService;

    public JsonGatewayController(GatewayFacadeService gatewayFacadeService) {
        this.gatewayFacadeService = gatewayFacadeService;
    }

    @PostMapping(value = "/current", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CurrentRateResponse current(@Valid @RequestBody CurrentRateRequest request) {
        return gatewayFacadeService.handleJsonCurrent(request);
    }

    @PostMapping(value = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HistoryRateResponse history(@Valid @RequestBody HistoryRateRequest request) {
        return gatewayFacadeService.handleJsonHistory(request);
    }
}
