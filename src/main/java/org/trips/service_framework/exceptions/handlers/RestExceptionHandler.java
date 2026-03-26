package org.trips.service_framework.exceptions.handlers;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.trips.service_framework.exceptions.RealmException;
import org.trips.service_framework.exceptions.ServiceException;
import org.trips.service_framework.models.responses.BaseResponse;
import org.trips.service_framework.models.responses.StatusResponse;
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
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        log.error("RestExceptionHandler: {}: ", ex.getClass().getSimpleName(), ex);
        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = String.format("%s: %s", ex.getClass().getSimpleName(), ex);
        log.error(bodyOfResponse);

        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        log.error("ServiceException: ", ex);

        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException: ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StatusResponse statusResponse = StatusResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusMessage(errors.toString())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(statusResponse)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {RealmException.class})
    protected ResponseEntity<Object> handleRealmException(RealmException ex, WebRequest request) {
        StatusResponse status = StatusResponse.builder()
                .statusCode(ex.getResponse().status())
                .statusMessage(ex.getResponse().reason())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(ex.getResponse().status()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StatusResponse statusResponse = StatusResponse.builder()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(statusResponse)
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StatusResponse statusResponse = StatusResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(statusResponse)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StatusResponse statusResponse = StatusResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .statusMessage(ex.getMessage())
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(statusResponse)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
