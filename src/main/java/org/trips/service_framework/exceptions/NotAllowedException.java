package org.trips.service_framework.exceptions;

/**
 * @author Abhinav Tripathi 25/04/20
 */
public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }
}
