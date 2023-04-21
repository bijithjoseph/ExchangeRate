package eu.nets.uni.apps.settlement.interview.driver;

import eu.nets.uni.apps.settlement.interview.driver.configuration.DriverAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(DriverAppProperties.class)
@EnableScheduling
public class DriverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverApplication.class, args);
	}

}
