package eu.nets.uni.apps.settlement.interview.driver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.nets.uni.apps.settlement.interview.driver.kafka.KafkaProducer;
import eu.nets.uni.apps.settlement.interview.driver.model.ExchangeRateEntry;
import eu.nets.uni.apps.settlement.interview.driver.model.ExchangeRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Value("classpath:sample-rates.json")
    Resource resourceFile;

    @Scheduled(cron = "${interview.exchange-rates-interval}")
    public void produceRates() {
        List<ExchangeRateEntry> exchangeRateEntries = loadSampleRates();
        exchangeRateEntries.forEach(exchangeRateEntry -> {
            BigDecimal rate = exchangeRateEntry.getRate();
            BigDecimal newRate = newRate(rate);

            exchangeRateEntry.setRate(newRate);
        });

        kafkaProducer.publish(ExchangeRates.builder()
                .baseCurrency("EUR")
                .timestamp(clock.instant())
                .exchangeRateEntries(exchangeRateEntries)
                .build());
    }

    /**
     * Load the dataset template
     * @return
     */
    private List<ExchangeRateEntry> loadSampleRates() {
        try (InputStream input = resourceFile.getInputStream()){
            TypeReference<List<ExchangeRateEntry>> typeReference = new TypeReference<>() {};
            return objectMapper.readValue(input, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Just for purpose of generating new rate
     * @param oldRate
     * @return
     */
    private BigDecimal newRate(BigDecimal oldRate) {
        return oldRate.add(BigDecimal.valueOf(myRandom(-0.1, 0.1)))
                .setScale(4, RoundingMode.HALF_UP);
    }

    private double myRandom(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
}
