package org.trips.service_framework.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends GeneralException {
    public static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
