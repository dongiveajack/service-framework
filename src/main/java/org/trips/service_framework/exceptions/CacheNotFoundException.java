package org.trips.service_framework.exceptions;

/**
 * @author Abhinav Tripathi 04/07/23
 */
public class CacheNotFoundException extends RuntimeException {
    public CacheNotFoundException(String message) {
        super(message);
    }

    public static CacheNotFoundException ofName(String cacheName) {
        return new CacheNotFoundException(String.format("Cache with name %s not found", cacheName));
    }
}
