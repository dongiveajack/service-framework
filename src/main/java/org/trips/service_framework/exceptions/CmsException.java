package org.trips.service_framework.exceptions;

import org.springframework.http.HttpStatus;

public class CmsException extends GeneralException {

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public CmsException(String s) {
        super(s);
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }
}