package org.trips.service_framework.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Abhinav Tripathi 08/06/21
 */
public class AccessDeniedException extends GeneralException {

    public AccessDeniedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
