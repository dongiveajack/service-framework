package com.avail.service_framework.exceptions.handlers;

import com.avail.service_framework.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created By Abhinav Tripathi on 2019-09-23
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        log.error("Error : ", ex);
        String bodyOfResponse = "Error Occurred " + ex.toString();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(RuntimeException ex, WebRequest request) {
        log.error("Error : ", ex);
        String bodyOfResponse = "No Such Element Exception " + ex.toString();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        log.error("Error : ", ex);
        String bodyOfResponse = "Service Exception " + ex.toString();

        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Error : ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return handleExceptionInternal(ex, errors,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
