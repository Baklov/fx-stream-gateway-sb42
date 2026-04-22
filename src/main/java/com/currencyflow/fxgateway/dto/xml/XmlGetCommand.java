package com.currencyflow.fxgateway.dto.xml;

import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlGetCommand {
    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;
    @JacksonXmlProperty(localName = "currency")
    private String currency;

    public String getConsumer() { return consumer; }
    public void setConsumer(String consumer) { this.consumer = consumer; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
