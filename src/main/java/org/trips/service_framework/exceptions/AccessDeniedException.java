package org.trips.service_framework.exceptions;

/**
 * @author Abhinav Tripathi 08/06/21
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
