package eu.nets.uni.apps.settlement.interview.driver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class DriverAppConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
