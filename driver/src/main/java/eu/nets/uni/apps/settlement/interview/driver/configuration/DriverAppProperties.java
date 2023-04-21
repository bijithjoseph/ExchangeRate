package eu.nets.uni.apps.settlement.interview.driver.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "interview")
@Data
public class DriverAppProperties {

    private String kafkaTopicExchangeRates;

}
