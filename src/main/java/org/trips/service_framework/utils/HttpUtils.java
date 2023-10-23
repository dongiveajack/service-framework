package org.trips.service_framework.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.trips.service_framework.exceptions.NotAllowedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Abhinav Tripathi 04/11/22
 */
public class HttpUtils {
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String readHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    public static String readMandatoryHeader(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName)).orElseThrow(() -> new NotAllowedException(String.format("%s header is missing", headerName)));
    }
}
