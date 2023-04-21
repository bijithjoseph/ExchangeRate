package eu.nets.uni.apps.settlement.interview.document;

import eu.nets.uni.apps.settlement.interview.model.Currency;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "ExchangeRate")
public class ExchangeRateDocument {

    @Transient
    public static final String EXCHANGE_RATE_ID_SEQUENCE = "exchange_rate_id_sequence";
    @Id
    private Long id;
    private LocalDateTime timestamp;
    private Currency baseCurrency;
    private List<ExchangeRateEntry> exchangeRateEntries;
}