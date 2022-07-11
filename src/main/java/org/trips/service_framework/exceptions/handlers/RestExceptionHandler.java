package org.trips.service_framework.exceptions.handlers;

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
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        log.error("Error : ", ex);
        String errMsg = "Error Occurred " + ex.getMessage();
        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(errMsg)
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(RuntimeException ex, WebRequest request) {
        log.error("Error : ", ex);
        String bodyOfResponse = "No Such Element Exception " + ex.toString();

        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .statusMessage(bodyOfResponse)
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        log.error("Error : ", ex);
        String bodyOfResponse = "Service Exception " + ex.toString();

        StatusResponse status = StatusResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(bodyOfResponse)
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(status)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

        String bodyOfResponse = "Error " + errors.toString();

        StatusResponse statusResponse = StatusResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusMessage(bodyOfResponse)
                .statusType(StatusResponse.Type.ERROR)
                .build();

        BaseResponse response = BaseResponse.builder()
                .status(statusResponse)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
