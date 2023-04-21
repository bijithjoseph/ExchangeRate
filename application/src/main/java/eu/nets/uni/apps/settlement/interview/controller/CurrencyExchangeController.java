package eu.nets.uni.apps.settlement.interview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.nets.uni.apps.settlement.interview.api.ExchangeRatesApi;
import eu.nets.uni.apps.settlement.interview.exception.ReportGenerationException;
import eu.nets.uni.apps.settlement.interview.model.Currency;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.model.ExchangeSummary;
import eu.nets.uni.apps.settlement.interview.service.ExchangeRateFetchService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
public class CurrencyExchangeController implements ExchangeRatesApi {
    private final ExchangeRateFetchService exchangeRateFetchService;

    private final MappingJackson2XmlHttpMessageConverter xmlMapperProvider;

    public CurrencyExchangeController(ExchangeRateFetchService exchangeRateFetchService, MappingJackson2XmlHttpMessageConverter xmlMapperProvider) {
        this.exchangeRateFetchService = exchangeRateFetchService;
        this.xmlMapperProvider = xmlMapperProvider;
    }

    @Override
    public ResponseEntity<Resource> generateAverageCurrencyRateReport(Currency baseCurrency, UUID xRequestID) {
        if (null == xRequestID) {
            xRequestID = UUID.randomUUID();
        }
        String xml;
        try {
            xml = xmlMapperProvider.getObjectMapper().writeValueAsString(exchangeRateFetchService.generateAverageCurrencyRate(baseCurrency, xRequestID));
        } catch (JsonProcessingException e) {
            throw new ReportGenerationException("Error occurred while report generation", xRequestID, e);
        }
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xml");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(xml.getBytes().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @Override
    public ResponseEntity<ExchangeSummary> getCurrencyExchangeQuote(Currency baseCurrency, Currency quoteCurrency, BigDecimal baseCurrencyAmount, UUID xRequestID) {
        if (null == xRequestID) {
            xRequestID = UUID.randomUUID();
        }
        return new ResponseEntity<>(exchangeRateFetchService.getExchangeQuote(baseCurrency, quoteCurrency, baseCurrencyAmount, xRequestID), getResponseHeaders(xRequestID), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ExchangeRateDetail> getExchangeRate(Currency baseCurrency, UUID xRequestID, OffsetDateTime dateTime) {
        if (null == xRequestID) {
            xRequestID = UUID.randomUUID();
        }
        return new ResponseEntity<>(exchangeRateFetchService.getExchangeRate(baseCurrency, dateTime, xRequestID), getResponseHeaders(xRequestID), HttpStatus.OK);
    }

    private HttpHeaders getResponseHeaders(UUID xRequestID) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-ID", String.valueOf(xRequestID));
        return headers;
    }
}
