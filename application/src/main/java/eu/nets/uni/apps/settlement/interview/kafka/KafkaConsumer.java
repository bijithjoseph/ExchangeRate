package eu.nets.uni.apps.settlement.interview.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.nets.uni.apps.settlement.interview.exception.DataProcessingException;
import eu.nets.uni.apps.settlement.interview.service.ExchangeRateUpdateService;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper objectMapper;
    private final ExchangeRateUpdateService exchangeRateUpdateService;

    public KafkaConsumer(ObjectMapper mapper, ExchangeRateUpdateService exchangeRateUpdateService) {
        this.objectMapper = mapper;
        this.exchangeRateUpdateService = exchangeRateUpdateService;
    }
    @KafkaListener(
            id="exchange-rates",
            topics = "${interview.kafka-topic-exchange-rates}")
    void consume(String jsonMessage) {
        logger.info("Got message: {}", jsonMessage);

        ExchangeRateDetail exchangeRate = null;
        try {
            exchangeRate = objectMapper.readValue(jsonMessage, ExchangeRateDetail.class);
        } catch (JsonProcessingException e) {
            logger.error("Error while receiving message");
            throw new DataProcessingException("Error while receiving message",e);
        }
        exchangeRateUpdateService.updateExchangeRates(exchangeRate);

    }
}
