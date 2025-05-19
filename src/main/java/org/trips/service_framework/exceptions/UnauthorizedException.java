package org.trips.service_framework.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Abhinav Tripathi 19/05/25
 */
@NoArgsConstructor
public class UnauthorizedException extends GeneralException {
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
