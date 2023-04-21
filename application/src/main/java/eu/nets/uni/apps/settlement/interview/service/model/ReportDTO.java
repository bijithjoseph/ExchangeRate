package eu.nets.uni.apps.settlement.interview.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReportDTO {
    private String baseCurrency;
    private String quoteCurrency;
    private String time;
    private double rate;
}
