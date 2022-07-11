package org.trips.service_framework.exceptions;

/**
 * Created By Abhinav Tripathi on 2019-08-30
 */
public class InternalAuthenticationServiceException extends Exception {
    public InternalAuthenticationServiceException(String message) {
        super(message);
    }

    public InternalAuthenticationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalAuthenticationServiceException(Throwable cause) {
        super(cause);
    }

    protected InternalAuthenticationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
