package org.trips.service_framework.exceptions.handlers;

import org.trips.service_framework.exceptions.ServiceException;
import org.trips.service_framework.exceptions.annotations.GrpcExceptionHandler;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

/**
 * @author Abhinav Tripathi 19/05/20
 */
@Slf4j
@Component
public class BaseGrpcExceptionHandler {
    @GrpcExceptionHandler(ServiceException.class)
    public Status handleServiceException(ServiceException ex) {
        log.error("Error : ", ex);
        return Status.INTERNAL.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    public Status handleNoSuchElementException(RuntimeException ex) {
        log.error("Error : ", ex);
        return Status.NOT_FOUND.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(value = {Exception.class})
    public Status handleUnknownError(Exception ex) {
        log.error("Error : ", ex);
        return Status.INTERNAL.withDescription(ex.getMessage());
    }
}
