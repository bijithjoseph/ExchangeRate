package eu.nets.uni.apps.settlement.interview.exception;

public class ErrorCode {

    private ErrorCode() {
        throw new IllegalStateException("Error code constants");
    }

    public static Integer NOT_FOUND = 1001;
    public static Integer REPORT_ERROR = 1002;
}
