package com.currencyflow.fxgateway.dto.xml;


import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "command")
public class XmlCommandRequest {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;
    @JacksonXmlProperty(localName = "get")
    private XmlGetCommand get;
    @JacksonXmlProperty(localName = "history")
    private XmlHistoryCommand history;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public XmlGetCommand getGet() { return get; }
    public void setGet(XmlGetCommand get) { this.get = get; }
    public XmlHistoryCommand getHistory() { return history; }
    public void setHistory(XmlHistoryCommand history) { this.history = history; }
}
