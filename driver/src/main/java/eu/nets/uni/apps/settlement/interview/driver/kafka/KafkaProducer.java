package eu.nets.uni.apps.settlement.interview.driver.kafka;

import eu.nets.uni.apps.settlement.interview.driver.configuration.DriverAppProperties;
import eu.nets.uni.apps.settlement.interview.driver.model.ExchangeRates;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, ExchangeRates> kafkaTemplate;
    private final DriverAppProperties driverAppProperties;

    public void publish(ExchangeRates exchangeRates) {
        kafkaTemplate.send(driverAppProperties.getKafkaTopicExchangeRates(), exchangeRates);
    }
}
