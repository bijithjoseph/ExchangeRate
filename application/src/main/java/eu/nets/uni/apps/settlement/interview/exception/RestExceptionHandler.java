package eu.nets.uni.apps.settlement.interview.exception;

import eu.nets.uni.apps.settlement.interview.model.Error;
import eu.nets.uni.apps.settlement.interview.service.ExchangeRateFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ControllerAdvice
public class RestExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    ResponseEntity<Error> handleException(ExchangeRateNotFoundException e){
        logger.error("Error response generated for Exception",e);
        Error errorResponse = new Error();
        errorResponse.setCode(ErrorCode.NOT_FOUND);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse,getResponseHeaders(e.getxRequestID()),HttpStatus.NOT_FOUND);
    }
    @ResponseBody
    ResponseEntity<Error> handleException(ReportGenerationException e){
        logger.error("Error response generated for Exception",e);
        Error errorResponse = new Error();
        errorResponse.setCode(ErrorCode.REPORT_ERROR);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse,getResponseHeaders(e.getxRequestId()),HttpStatus.EXPECTATION_FAILED);
    }

    private HttpHeaders getResponseHeaders(UUID xRequestID) {
        if(null == xRequestID){
            xRequestID = UUID.randomUUID();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-ID", String.valueOf(xRequestID));
        return headers;
    }
}
