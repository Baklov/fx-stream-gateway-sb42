package com.currencyflow.fxgateway.dto.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "response")
public class XmlCommandResponse {
    private String requestId;
    private String client;
    private String type;
    private XmlCurrentRatePayload current;

    @JacksonXmlElementWrapper(localName = "historyItems")
    @JacksonXmlProperty(localName = "item")
    private List<XmlHistoryRatePayload> historyItems;

    public XmlCommandResponse() {}
    public XmlCommandResponse(String requestId, String client, String type, XmlCurrentRatePayload current, List<XmlHistoryRatePayload> historyItems) {
        this.requestId = requestId;
        this.client = client;
        this.type = type;
        this.current = current;
        this.historyItems = historyItems;
    }

    public String getRequestId() { return requestId; }
    public String getClient() { return client; }
    public String getType() { return type; }
    public XmlCurrentRatePayload getCurrent() { return current; }
    public List<XmlHistoryRatePayload> getHistoryItems() { return historyItems; }
}
