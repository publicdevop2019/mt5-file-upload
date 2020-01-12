package com.hw.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TransactionSystemException.class,
            IllegalArgumentException.class,
            DataIntegrityViolationException.class,
    })
    protected ResponseEntity<?> handleException(RuntimeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();

        String[] split = NestedExceptionUtils.getMostSpecificCause(ex).getMessage().replace("\t", "").split("\n");

        String s = UUID.randomUUID().toString();
        body.put("errors", split);
        body.put("error_id", s);
        log.error("Handled exception UUID - {} - class - [{}] - Exception :", s, ex.getClass(), ex);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Order()
    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<?> defaultHandleException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> split;
        if(NestedExceptionUtils.getMostSpecificCause(ex).getMessage() != null){
            split = List.of(NestedExceptionUtils.getMostSpecificCause(ex).getMessage().replace("\t", "").split("\n"));
        }else{
            split = List.of("Unable to get most specific cause, see log");
        }
        String s = UUID.randomUUID().toString();
        body.put("errors", split);
        body.put("error_id", s);
        log.error("Unhandled exception UUID - {} - class - [{}] - Exception :", s, ex.getClass(), ex);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
