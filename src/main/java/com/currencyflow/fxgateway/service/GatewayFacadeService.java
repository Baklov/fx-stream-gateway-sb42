package com.currencyflow.fxgateway.service;

import com.currencyflow.fxgateway.dto.*;
import com.currencyflow.fxgateway.dto.xml.*;
import com.currencyflow.fxgateway.entity.CurrencySnapshotEntity;
import com.currencyflow.fxgateway.exception.GatewayBadRequestException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayFacadeService {
    private static final String EXT_SERVICE_1 = "EXT_SERVICE_1";
    private static final String EXT_SERVICE_2 = "EXT_SERVICE_2";

    private final DuplicateRequestGuard duplicateRequestGuard;
    private final InboundAuditService inboundAuditService;
    private final CurrencyQueryService currencyQueryService;

    public GatewayFacadeService(DuplicateRequestGuard duplicateRequestGuard, InboundAuditService inboundAuditService, CurrencyQueryService currencyQueryService) {
        this.duplicateRequestGuard = duplicateRequestGuard;
        this.inboundAuditService = inboundAuditService;
        this.currencyQueryService = currencyQueryService;
    }

    public CurrentRateResponse handleJsonCurrent(CurrentRateRequest request) {
        duplicateRequestGuard.ensureNewRequest(request.getRequestId());
        inboundAuditService.storeAudit(request.getRequestId(), EXT_SERVICE_1, Instant.ofEpochMilli(request.getTimestamp()), request.getClient(), "CURRENT", request.getCurrency());

        CurrencySnapshotEntity item = currencyQueryService.getLatest(request.getCurrency());
        return new CurrentRateResponse(request.getRequestId(), request.getClient(), item.getCurrencyCode(), item.getBaseCurrency(), item.getRateValue(), item.getSnapshotTime());
    }

    public HistoryRateResponse handleJsonHistory(HistoryRateRequest request) {
        duplicateRequestGuard.ensureNewRequest(request.getRequestId());
        inboundAuditService.storeAudit(request.getRequestId(), EXT_SERVICE_1, Instant.ofEpochMilli(request.getTimestamp()), request.getClient(), "HISTORY", request.getCurrency());

        List<HistoryRateItem> items = currencyQueryService.getHistory(request.getCurrency(), request.getPeriod())
                .stream()
                .map(row -> new HistoryRateItem(row.getCurrencyCode(), row.getBaseCurrency(), row.getRateValue(), row.getSnapshotTime()))
                .collect(Collectors.toList());

        return new HistoryRateResponse(request.getRequestId(), request.getClient(), request.getCurrency(), request.getPeriod(), items);
    }

    public XmlCommandResponse handleXml(XmlCommandRequest request) {
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new GatewayBadRequestException("XML command id is required");
        }

        duplicateRequestGuard.ensureNewRequest(request.getId());

        if (request.getGet() != null) {
            XmlGetCommand command = request.getGet();
            inboundAuditService.storeAudit(request.getId(), EXT_SERVICE_2, Instant.now(), command.getConsumer(), "CURRENT", command.getCurrency());
            CurrencySnapshotEntity item = currencyQueryService.getLatest(command.getCurrency());

            return new XmlCommandResponse(
                    request.getId(),
                    command.getConsumer(),
                    "CURRENT",
                    new XmlCurrentRatePayload(item.getCurrencyCode(), item.getBaseCurrency(), item.getRateValue(), item.getSnapshotTime().toString()),
                    null
            );
        }

        if (request.getHistory() != null) {
            XmlHistoryCommand command = request.getHistory();
            inboundAuditService.storeAudit(request.getId(), EXT_SERVICE_2, Instant.now(), command.getConsumer(), "HISTORY", command.getCurrency());

            List<XmlHistoryRatePayload> items = currencyQueryService.getHistory(command.getCurrency(), command.getPeriod())
                    .stream()
                    .map(row -> new XmlHistoryRatePayload(row.getCurrencyCode(), row.getBaseCurrency(), row.getRateValue(), row.getSnapshotTime().toString()))
                    .collect(Collectors.toList());

            return new XmlCommandResponse(request.getId(), command.getConsumer(), "HISTORY", null, items);
        }

        throw new GatewayBadRequestException("XML command must contain either <get> or <history>");
    }
}
