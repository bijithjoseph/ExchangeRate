package eu.nets.uni.apps.settlement.interview.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, JsonProcessingException e) {
        super(message, e);
    }
}
