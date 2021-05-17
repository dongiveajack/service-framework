package com.avail.service_framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 2019-08-30
 */
@Slf4j
public class HttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = request.getHeader("Authorization");
        Context.setContext(Objects.nonNull(requestId) ? requestId : "System");
        return Boolean.TRUE;
    }
}
