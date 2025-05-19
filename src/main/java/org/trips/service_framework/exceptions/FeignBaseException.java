package org.trips.service_framework.exceptions;

import feign.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Anupam Dagar on 16/05/24
 */

@Getter
public abstract class FeignBaseException extends GeneralException {

    private Response response;

    public FeignBaseException(Response response) {
        super();
        this.response = response;
    }

    public FeignBaseException(Response response, String message) {
        super(message);
        this.response = response;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.resolve(response.status());
    }
}
