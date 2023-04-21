package eu.nets.uni.apps.settlement.interview.repository;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRateRepository extends MongoRepository<ExchangeRateDocument, Long> {
    Optional<ExchangeRateDocument> findFirstByBaseCurrencyOrderByTimestampDesc(Currency baseCurrency);

    Optional<ExchangeRateDocument> findFirstByBaseCurrencyAndTimestampOrderByTimestampDesc(Currency baseCurrency, LocalDateTime timeStamp);

    @Query(fields = "{ 'timestamp': 1,'baseCurrency':1, 'exchangeRateEntries.$': 1 }")
    Optional<ExchangeRateDocument> findFirstByBaseCurrencyAndExchangeRateEntriesCurrencyOrderByTimestampDesc(Currency baseCurrency, Currency currency);

    List<ExchangeRateDocument> findByBaseCurrencyAndTimestampGreaterThanOrderByTimestampAsc(Currency baseCurrency, LocalDateTime timeStamp);
}
