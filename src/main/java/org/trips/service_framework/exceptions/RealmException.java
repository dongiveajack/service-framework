package org.trips.service_framework.exceptions;

import feign.Response;
import org.springframework.http.HttpStatus;

public class RealmException extends FeignBaseException {
    public static final HttpStatus status = HttpStatus.UNAUTHORIZED;


    public RealmException(Response response) {
        super(response);
    }

    public RealmException(Response response, String message) {
        super(response, message);
    }
}
