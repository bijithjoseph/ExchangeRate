package eu.nets.uni.apps.settlement.interview.controller;

import eu.nets.uni.apps.settlement.interview.model.Currency;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.model.ExchangeSummary;
import eu.nets.uni.apps.settlement.interview.service.ExchangeRateFetchService;
import eu.nets.uni.apps.settlement.interview.service.model.ConversionRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyExchangeController.class)
class CurrencyExchangeControllerTest {
    private static final String UNKNOWN_CURRENCY_CODE = "UNKNOWN";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRateFetchService exchangeRateService;

    @Test
    void should_return_currency_exchange_rate() throws Exception {

        ExchangeRateDetail exchangeRates = new ExchangeRateDetail();
        exchangeRates.setBaseCurrency(Currency.EUR);

        when(exchangeRateService.getExchangeRate(eq(Currency.EUR), any(), any(UUID.class))).thenReturn(exchangeRates);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/" + Currency.EUR.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_return_correct_currency_amount() throws Exception {


        ExchangeSummary dto = new ExchangeSummary();
        dto.setBaseCurrency(Currency.EUR);
        dto.setQuoteCurrency(Currency.AUD);
        dto.setBaseAmount(BigDecimal.valueOf(100));
        dto.setExchangeRate(BigDecimal.valueOf(10));
        dto.setQuoteAmount(BigDecimal.valueOf(1000));

        when(exchangeRateService.getExchangeQuote(any(Currency.class), any(Currency.class), any(BigDecimal.class), any(UUID.class))).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/EUR/AUD?base_currency_amount=100").header("X-Request-ID", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void should_generate_xml_report() throws Exception {
        Map<String, Map<String, Double>> time = new HashMap<>();
        ConversionRate dto = new ConversionRate(time, Currency.EUR);
        when(exchangeRateService.generateAverageCurrencyRate(eq(Currency.EUR), any(UUID.class))).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/EUR/report").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());
    }


    @Test
    void should_return_400_bad_request_for_unknown_base_currency() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates/" + UNKNOWN_CURRENCY_CODE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}