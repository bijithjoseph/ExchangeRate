package eu.nets.uni.apps.settlement.interview.exception;

import java.util.UUID;

public class ReportGenerationException extends RuntimeException {
    private final UUID xRequestId;

    public ReportGenerationException(String message, UUID xRequestId, Throwable e) {
        super(message, e);
        this.xRequestId = xRequestId;
    }

    public UUID getxRequestId() {
        return xRequestId;
    }
}
