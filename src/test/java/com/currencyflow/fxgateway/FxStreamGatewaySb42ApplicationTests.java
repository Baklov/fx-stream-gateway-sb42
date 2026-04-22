package com.currencyflow.fxgateway;

import com.currencyflow.fxgateway.entity.CurrencySnapshotEntity;
import com.currencyflow.fxgateway.repository.CurrencySnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FxStreamGatewaySb42ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencySnapshotRepository currencySnapshotRepository;

    @BeforeEach
    void setUp() {
        currencySnapshotRepository.deleteAll();

        Instant now = Instant.parse("2026-04-21T23:30:00Z");
        Instant older = Instant.parse("2026-04-21T22:30:00Z");

        currencySnapshotRepository.save(new CurrencySnapshotEntity(
                "EUR", "EUR", new BigDecimal("1.00000000"), older
        ));
        currencySnapshotRepository.save(new CurrencySnapshotEntity(
                "EUR", "EUR", new BigDecimal("1.00000000"), now
        ));
        currencySnapshotRepository.save(new CurrencySnapshotEntity(
                "USD", "EUR", new BigDecimal("1.17386000"), older
        ));
        currencySnapshotRepository.save(new CurrencySnapshotEntity(
                "USD", "EUR", new BigDecimal("1.17400000"), now
        ));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnCurrentRateJson() throws Exception {
        String requestId = "json-current-" + UUID.randomUUID();

        String body = """
                {
                  "requestId": "%s",
                  "timestamp": 1776814200000,
                  "client": "test-client",
                  "currency": "EUR"
                }
                """.formatted(requestId);

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.client").value("test-client"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.baseCurrency").value("EUR"))
                .andExpect(jsonPath("$.rate").value(1.00000000));
    }

    @Test
    void shouldHandleHistoryJsonRequest() throws Exception {
        String requestId = "json-history-" + UUID.randomUUID();

        String body = """
                {
                  "requestId": "%s",
                  "timestamp": 1776814200000,
                  "client": "test-client",
                  "currency": "EUR",
                  "period": 2
                }
                """.formatted(requestId);

        MvcResult result = mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        int status = result.getResponse().getStatus();
        String response = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        assertThat(status).isIn(200, 404);
        assertThat(contentType).contains("application/json");

        if (status == 200) {
            assertThat(response).contains("\"requestId\":\"" + requestId + "\"");
            assertThat(response).contains("\"client\":\"test-client\"");
            assertThat(response).contains("\"currency\":\"EUR\"");
            assertThat(response).contains("\"items\"");
        } else {
            assertThat(response).contains("\"status\":404");
            assertThat(response).contains("\"error\":\"NOT_FOUND\"");
            assertThat(response).contains("No historical rates found for currency: EUR");
        }
    }

    @Test
    void shouldRejectDuplicateJsonRequestId() throws Exception {
        String requestId = "dup-" + UUID.randomUUID();

        String body = """
                {
                  "requestId": "%s",
                  "timestamp": 1776814200000,
                  "client": "test-client",
                  "currency": "EUR"
                }
                """.formatted(requestId);

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnCurrentRateXml() throws Exception {
        String requestId = "xml-current-" + UUID.randomUUID();

        String body = """
                <command id="%s">
                    <get consumer="xml-client" currency="EUR" />
                </command>
                """.formatted(requestId);

        mockMvc.perform(post("/xml_api/command")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("/response/requestId").string(requestId))
                .andExpect(xpath("/response/client").string("xml-client"))
                .andExpect(xpath("/response/type").string("CURRENT"))
                .andExpect(xpath("/response/current/currency").string("EUR"))
                .andExpect(xpath("/response/current/baseCurrency").string("EUR"));
    }

    @Test
    void shouldHandleHistoryXmlRequest() throws Exception {
        String requestId = "xml-history-" + UUID.randomUUID();

        String body = """
                <command id="%s">
                    <history consumer="xml-client" currency="EUR" period="200" />
                </command>
                """.formatted(requestId);

        MvcResult result = mockMvc.perform(post("/xml_api/command")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andReturn();

        int status = result.getResponse().getStatus();
        String response = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        assertThat(status).isIn(200, 404);
        assertThat(contentType).contains("application/xml");

        if (status == 200) {
            assertThat(response).contains("<requestId>" + requestId + "</requestId>");
            assertThat(response).contains("<client>xml-client</client>");
            assertThat(response).contains("<type>HISTORY</type>");
            assertThat(response).contains("<historyItems>");
            assertThat(response).contains("<currency>EUR</currency>");
            assertThat(response).contains("<baseCurrency>EUR</baseCurrency>");
        } else {
            assertThat(response).contains("No historical rates found for currency: EUR");
        }
    }

    @Test
    void shouldRejectDuplicateXmlRequestId() throws Exception {
        String requestId = "xml-dup-" + UUID.randomUUID();

        String body = """
                <command id="%s">
                    <get consumer="xml-client" currency="EUR" />
                </command>
                """.formatted(requestId);

        mockMvc.perform(post("/xml_api/command")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/xml_api/command")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isConflict());
    }
}