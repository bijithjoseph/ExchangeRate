package eu.nets.uni.apps.settlement.interview.driver.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ExchangeRates {
    private Instant timestamp;
    private String baseCurrency;
    private List<ExchangeRateEntry> exchangeRateEntries;
}
