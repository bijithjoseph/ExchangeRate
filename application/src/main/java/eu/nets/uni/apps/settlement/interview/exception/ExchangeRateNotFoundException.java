package eu.nets.uni.apps.settlement.interview.exception;

import java.util.UUID;

public class ExchangeRateNotFoundException extends RuntimeException {
    private final UUID xRequestID;

    public ExchangeRateNotFoundException(String message, UUID xRequestID) {
        super(message);
        this.xRequestID = xRequestID;
    }

    public UUID getxRequestID() {
        return xRequestID;
    }
}
