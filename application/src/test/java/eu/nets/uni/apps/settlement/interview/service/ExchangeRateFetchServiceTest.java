package eu.nets.uni.apps.settlement.interview.service;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.document.ExchangeRateEntry;
import eu.nets.uni.apps.settlement.interview.exception.ExchangeRateNotFoundException;
import eu.nets.uni.apps.settlement.interview.model.Currency;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.model.ExchangeSummary;
import eu.nets.uni.apps.settlement.interview.repository.CurrencyExchangeRateRepository;
import eu.nets.uni.apps.settlement.interview.service.mapper.ExchangeRateObjectMapper;
import eu.nets.uni.apps.settlement.interview.service.model.ConversionRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureDataMongo
class ExchangeRateFetchServiceTest {
    LocalDateTime time;
    @Autowired
    private CurrencyExchangeRateRepository repository;
    @Autowired
    private ExchangeRateObjectMapper exchangeRateMapper;
    @Value("${excharerate.report.period}")
    private Duration reportPeriod;
    @Autowired
    private ExchangeRateFetchService service;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll();
        time = LocalDateTime.now(ZoneId.of("UTC"));
        repository.save(generateExchangeRate(1L, time));
        repository.save(generateExchangeRate(2L, time.minus(10, ChronoUnit.MINUTES)));
        repository.save(generateExchangeRate(3L, time.minus(5, ChronoUnit.MINUTES)));
    }

    @Test
    void getExchangeRateInvalid() {
        UUID uuid = UUID.randomUUID();
        OffsetDateTime dateTime = time.atOffset(ZoneOffset.UTC).minus(5, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MILLIS);
        assertThrows(ExchangeRateNotFoundException.class, () -> service.getExchangeRate(Currency.EUR, dateTime, uuid));
    }

    @Test
    void getExchangeRateValid() {
        ExchangeRateDetail exchangeRate = service.getExchangeRate(Currency.EUR, time.atOffset(ZoneOffset.UTC).plus(2, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MILLIS), UUID.randomUUID());
        assertEquals(Currency.EUR, exchangeRate.getBaseCurrency());
    }

    @Test
    void getExchangeRate() {
        ExchangeRateDetail exchangeRate = service.getExchangeRate(Currency.EUR, time.atOffset(ZoneOffset.UTC).plus(2, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MILLIS), UUID.randomUUID());
        assertEquals(Currency.EUR, exchangeRate.getBaseCurrency());
    }

    @Test
    void getExchangeQuote() {
        ExchangeSummary summary = service.getExchangeQuote(Currency.EUR, Currency.AUD, BigDecimal.valueOf(10), UUID.randomUUID());
        assertNotNull(summary);
        assertEquals(Currency.EUR, summary.getBaseCurrency());
        assertEquals(Currency.AUD, summary.getQuoteCurrency());
        assertEquals(BigDecimal.valueOf(10), summary.getExchangeRate());
        assertEquals(BigDecimal.valueOf(10), summary.getBaseAmount());
        assertEquals(BigDecimal.valueOf(100), summary.getQuoteAmount());
    }

    @Test
    void generateAverageCurrencyRateReport() {
        ConversionRate averageCurrencyRate = service.generateAverageCurrencyRate(Currency.EUR, UUID.randomUUID());
        assertNotNull(averageCurrencyRate);
        assertEquals(1, averageCurrencyRate.getTime().size());
    }

    private ExchangeRateEntry createExchangeRateEntry(Currency currency, BigDecimal rate) {

        ExchangeRateEntry exchangeRateEntry = new ExchangeRateEntry();
        exchangeRateEntry.setCurrency(currency);
        exchangeRateEntry.setRate(rate);
        return exchangeRateEntry;
    }

    private ExchangeRateDocument generateExchangeRate(Long id, LocalDateTime time) {
        ExchangeRateDocument exchangeRateDocument = new ExchangeRateDocument();
        List<ExchangeRateEntry> exchangeRateEntries = Arrays.asList(createExchangeRateEntry(Currency.AUD, BigDecimal.valueOf(10))
                , createExchangeRateEntry(Currency.CHF, BigDecimal.valueOf(1)));
        exchangeRateDocument.setId(id);
        exchangeRateDocument.setBaseCurrency(Currency.EUR);
        exchangeRateDocument.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));
        exchangeRateDocument.setExchangeRateEntries(exchangeRateEntries);
        return exchangeRateDocument;
    }


}