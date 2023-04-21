package eu.nets.uni.apps.settlement.interview.kafka;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.model.Currency;
import eu.nets.uni.apps.settlement.interview.repository.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureDataMongo
class KafkaConsumerTest {
    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private CurrencyExchangeRateRepository currencyExchangeRateRepository;

    private static final String KAFKA_RECORD = "{\"timestamp\":1647695820.001478000,\"baseCurrency\":\"EUR\",\"exchangeRateEntries\":[{\"currency\":\"NOK\",\"rate\":10.5356}]}";
    private static final String INVALID_KAFKA_RECORD = "";

    @Test
    void consume_method_should_save_json_data_to_db(){
        kafkaConsumer.consume(KAFKA_RECORD);

        List<ExchangeRateDocument> exchangeRates = currencyExchangeRateRepository.findAll();

        assertEquals(1, exchangeRates.size());
        assertEquals(BigDecimal.valueOf(10.5356), exchangeRates.get(0).getExchangeRateEntries().get(0).getRate());
        assertEquals(Currency.EUR ,exchangeRates.get(0).getBaseCurrency());
    }

    @Test
    void consume_method_should_throw_on_invalid_string(){
        assertThrows(RuntimeException.class,()->kafkaConsumer.consume(INVALID_KAFKA_RECORD));
    }
}