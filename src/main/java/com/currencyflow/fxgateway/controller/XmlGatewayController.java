package com.currencyflow.fxgateway.controller;

import com.currencyflow.fxgateway.dto.xml.XmlCommandRequest;
import com.currencyflow.fxgateway.dto.xml.XmlCommandResponse;
import com.currencyflow.fxgateway.service.GatewayFacadeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xml_api", produces = MediaType.APPLICATION_XML_VALUE)
public class XmlGatewayController {
    private final GatewayFacadeService gatewayFacadeService;

    public XmlGatewayController(GatewayFacadeService gatewayFacadeService) {
        this.gatewayFacadeService = gatewayFacadeService;
    }

    @PostMapping(value = "/command", consumes = MediaType.APPLICATION_XML_VALUE)
    public XmlCommandResponse command(@RequestBody XmlCommandRequest request) {
        return gatewayFacadeService.handleXml(request);
    }
}
