package org.trips.service_framework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created By Anupam Dagar on 16/05/24
 */
public abstract class GeneralException extends RuntimeException {

    public GeneralException() {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }

    abstract public HttpStatus getStatus();
}
