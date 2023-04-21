package eu.nets.uni.apps.settlement.interview.service.mapper;

import eu.nets.uni.apps.settlement.interview.document.ExchangeRateDocument;
import eu.nets.uni.apps.settlement.interview.model.ExchangeRateDetail;
import eu.nets.uni.apps.settlement.interview.model.ExchangeSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = OffsetDateTimeMapper.class)
public interface ExchangeRateObjectMapper {
    ExchangeRateDetail mapToExchangeRateDetail(ExchangeRateDocument exchangeRateDocument);

    @Mapping(target = "id", source = "documentId")
    ExchangeRateDocument mapToExchangeRateDocument(ExchangeRateDetail exchangeRateDetail, Long documentId);

    @Mapping(target = "quoteCurrency", expression = "java(exchangeDocument.getExchangeRateEntries().get(0).getCurrency())")
    @Mapping(target = "exchangeRate", expression = "java(exchangeDocument.getExchangeRateEntries().get(0).getRate())")
    @Mapping(target = "baseAmount", source = "baseCurrencyAmount")
    @Mapping(target = "quoteAmount", expression = "java(baseCurrencyAmount.multiply(exchangeDocument.getExchangeRateEntries().get(0).getRate()))")
    @Mapping(target = "quoteTime", source = "exchangeDocument.timestamp")
    ExchangeSummary mapToExchangeSummary(ExchangeRateDocument exchangeDocument, BigDecimal baseCurrencyAmount);

}
