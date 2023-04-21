package eu.nets.uni.apps.settlement.interview.service;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.document.IDSequences;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.repository.CurrencyExchangeRateRepository;
import eu.nets.uni.apps.settlement.interview.service.mapper.ExchangeRateObjectMapper;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class ExchangeRateUpdateService {

    private final CurrencyExchangeRateRepository repository;
    private final ExchangeRateObjectMapper exchangeRateMapper;

    private final MongoTemplate mongoTemplate;

    public ExchangeRateUpdateService(CurrencyExchangeRateRepository repository, ExchangeRateObjectMapper exchangeRateMapper, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.exchangeRateMapper = exchangeRateMapper;
        this.mongoTemplate = mongoTemplate;
    }

    public void updateExchangeRates(ExchangeRateDetail exchangeRate) {
        repository.save(exchangeRateMapper.mapToExchangeRateDocument(exchangeRate, generateDocumentId()));
    }

    private Long generateDocumentId() {
        Query query = new Query(Criteria.where("id").is(ExchangeRateDocument.EXCHANGE_RATE_ID_SEQUENCE));
        Update update = new Update().inc("sequenceNumber", 1);
        FindAndModifyOptions options = options().returnNew(true).upsert(true);
        IDSequences idSequence = mongoTemplate.findAndModify(query, update, options, IDSequences.class);
        return Objects.isNull(idSequence) ? 1 : idSequence.getSequenceNumber();
    }
}
