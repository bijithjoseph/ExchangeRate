package eu.nets.uni.apps.settlement.interview;

import eu.nets.uni.apps.settlement.interview.controller.CurrencyExchangeController;
import eu.nets.uni.apps.settlement.interview.service.ExchangeRateFetchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class InterviewApplicationTests {
    @Autowired
    private CurrencyExchangeController myController;

    @Autowired
    private ExchangeRateFetchService myService;

    @Test
    void contextLoads() throws Exception {
        assertThat(myController).isNotNull();
        assertThat(myService).isNotNull();
    }

    @Test
    void main() {
        InterviewApplication.main(new String[]{});
        assertThat(myController).isNotNull();
    }

}
