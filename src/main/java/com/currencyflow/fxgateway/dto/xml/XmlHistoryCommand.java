package com.currencyflow.fxgateway.dto.xml;


import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlHistoryCommand {
    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;
    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;
    @JacksonXmlProperty(isAttribute = true, localName = "period")
    private Integer period;

    public String getConsumer() { return consumer; }
    public void setConsumer(String consumer) { this.consumer = consumer; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }
}
