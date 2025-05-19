package org.trips.service_framework.exceptions.annotations;

import feign.Response;

/**
 * @author Anupam Dagar on 16/05/24
 */

public interface FeignClientExceptionHandler {
    Exception handle(Response response);
}
