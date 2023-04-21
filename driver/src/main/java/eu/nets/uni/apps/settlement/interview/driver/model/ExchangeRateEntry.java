package eu.nets.uni.apps.settlement.interview.driver.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRateEntry {
    private String currency;
    private BigDecimal rate;
}
