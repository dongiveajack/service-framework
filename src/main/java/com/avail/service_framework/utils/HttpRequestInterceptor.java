package com.avail.service_framework.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 2019-08-30
 */
public class HttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = request.getHeader("X-Requested-By");
        Context.setContext(Objects.nonNull(requestId) ? Long.valueOf(requestId) : 0L);
        return Boolean.TRUE;
    }
}
