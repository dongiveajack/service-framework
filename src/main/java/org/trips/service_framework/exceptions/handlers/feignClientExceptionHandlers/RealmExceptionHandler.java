package org.trips.service_framework.exceptions.handlers.feignClientExceptionHandlers;

import feign.Response;
import org.springframework.stereotype.Component;
import org.trips.service_framework.exceptions.RealmException;
import org.trips.service_framework.exceptions.annotations.FeignClientExceptionHandler;

@Component
public class RealmExceptionHandler implements FeignClientExceptionHandler {
    @Override
    public Exception handle(Response response) {
        return new RealmException(response);
    }
}
