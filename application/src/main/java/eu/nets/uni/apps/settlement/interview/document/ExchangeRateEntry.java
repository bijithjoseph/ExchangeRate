package eu.nets.uni.apps.settlement.interview.document;

import eu.nets.uni.apps.settlement.interview.model.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateEntry {
    private Currency currency;
    private BigDecimal rate;
}
