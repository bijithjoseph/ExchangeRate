package eu.nets.uni.apps.settlement.interview.service;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.exception.ExchangeRateNotFoundException;
import eu.nets.uni.apps.settlement.interview.model.Currency;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.model.ExchangeSummary;
import eu.nets.uni.apps.settlement.interview.service.model.XmlObjectDTO;
import eu.nets.uni.apps.settlement.interview.repository.CurrencyExchangeRateRepository;
import eu.nets.uni.apps.settlement.interview.service.mapper.ExchangeRateObjectMapper;
import eu.nets.uni.apps.settlement.interview.service.model.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
public class ExchangeRateFetchService {
    Logger logger = LoggerFactory.getLogger(ExchangeRateFetchService.class);
    private final CurrencyExchangeRateRepository repository;
    private final ExchangeRateObjectMapper exchangeRateMapper;
    @Value("${excharerate.report.period}")
    private Duration reportPeriod;


    public ExchangeRateFetchService(CurrencyExchangeRateRepository repository, ExchangeRateObjectMapper exchangeRateMapper) {
        this.repository = repository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public ExchangeRateDetail getExchangeRate(Currency baseCurrency, OffsetDateTime dateTime, UUID xRequestID) {
        logger.info("retrieving exchange rate base currency {} ,dateTime {} ,request id {}",baseCurrency,dateTime,xRequestID);

        Optional<ExchangeRateDocument> rate;
        if(!Objects.isNull(dateTime)) {
            rate = repository.findFirstByBaseCurrencyAndTimestampOrderByTimestampDesc(baseCurrency, dateTime.toLocalDateTime());
        }else{
            rate = repository.findFirstByBaseCurrencyOrderByTimestampDesc(baseCurrency);
        }

        if(rate.isPresent()){
        return exchangeRateMapper.mapToExchangeRateDetail(rate.get());
        }
        else{
            throw new ExchangeRateNotFoundException(String.format("Could not find exchange rate for rate %s ",baseCurrency), xRequestID);
        }
    }

    public ExchangeSummary getExchangeQuote(Currency baseCurrency, Currency quoteCurrency, BigDecimal baseCurrencyAmount, UUID xRequestID) {
        logger.info("retrieving exchange exchange quote-  base currency {}, quote currency {} ,Amount {} ,request id {}",baseCurrency,quoteCurrency,baseCurrencyAmount,xRequestID);
        Optional<ExchangeRateDocument> exchangeDocument = repository.findFirstByBaseCurrencyAndExchangeRateEntriesCurrencyOrderByTimestampDesc(baseCurrency, quoteCurrency);
        if(exchangeDocument.isPresent()){
            return exchangeRateMapper.mapToExchangeSummary(exchangeDocument.get(), baseCurrencyAmount);
        }else {
            throw new ExchangeRateNotFoundException(String.format("could not retrieve exchange rate for request %s",xRequestID),xRequestID);
        }

    }


    public XmlObjectDTO generateAverageCurrencyRate(Currency baseCurrency, UUID xRequestID) {
        logger.info("Generating exchange rate report for base currency {}, requestId {}",baseCurrency, xRequestID);
        LocalDateTime fromTime = LocalDateTime.now(ZoneId.systemDefault()).minus(reportPeriod);
        List<ExchangeRateDocument> rates = repository.findByBaseCurrencyAndTimestampGreaterThanOrderByTimestampAsc(baseCurrency, fromTime);

        Map<String, Map<String, Double>> conversionRatesAvg = rates.stream()
                .map(rate -> rate.getExchangeRateEntries().stream()
                        .map(entry->new ReportDTO(String.valueOf(rate.getBaseCurrency()), String.valueOf(entry.getCurrency())
                                ,"Interval-"+rate.getTimestamp().atZone(ZoneOffset.UTC).getHour() + "-" + rate.getTimestamp().atZone(ZoneOffset.UTC).getMinute()
                                ,entry.getRate().doubleValue())).collect(toList()))
                .collect(toList()).stream()
                .flatMap(Collection::stream)
                .collect(groupingBy(ReportDTO::getTime
                        , groupingBy(ReportDTO::getQuoteCurrency, averagingDouble(ReportDTO::getRate))));

        return new XmlObjectDTO(conversionRatesAvg, baseCurrency);
    }
}
