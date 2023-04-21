package eu.nets.uni.apps.settlement.interview.service.model;

import eu.nets.uni.apps.settlement.interview.model.Currency;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@Getter
@XmlRootElement(name = "Conversion-Report")
public class ConversionRate {
    private final Currency baseCurrency;
    private final Map<String, Map<String, Double>> time;

    public ConversionRate(Map<String, Map<String, Double>> conversionRatesAvg, Currency baseCurrency) {
        this.time = conversionRatesAvg;
        this.baseCurrency = baseCurrency;
    }

}
