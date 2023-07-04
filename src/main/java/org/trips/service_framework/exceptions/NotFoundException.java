package org.trips.service_framework.exceptions;

/**
 * @author Abhinav Tripathi 25/04/20
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
