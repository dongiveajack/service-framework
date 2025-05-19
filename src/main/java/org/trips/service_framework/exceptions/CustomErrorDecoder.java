package org.trips.service_framework.exceptions;

import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.trips.service_framework.aop.FeignClientAdvice;
import org.trips.service_framework.exceptions.annotations.FeignClientExceptionHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author anomitra on 17/04/24
 */

@Component("customErrorDecoder")
@RequiredArgsConstructor
public class CustomErrorDecoder implements ErrorDecoder {
    private final ApplicationContext applicationContext;
    private final Map<String, FeignClientExceptionHandler> exceptionHandlerMap = new HashMap<>();
    private final Default defaultDecoder = new Default();

    @EventListener
    public void initialize(ApplicationReadyEvent evt) {
        Map<String, Object> feignClients = applicationContext.getBeansWithAnnotation(FeignClient.class);
        List<Method> clientMethods = feignClients.values().stream()
                .map(Object::getClass)
                .map(aClass -> aClass.getInterfaces()[0])
                .map(ReflectionUtils::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        for (Method m : clientMethods) {
            String configKey = Feign.configKey(m.getDeclaringClass(), m);
            FeignClientAdvice handlerAnnotation = getFeignClientAdviceAnnotation(m);
            if (Objects.nonNull(handlerAnnotation)) {
                FeignClientExceptionHandler handler = applicationContext.getBean(handlerAnnotation.value());
                exceptionHandlerMap.put(configKey, handler);
            }
        }

    }
    private FeignClientAdvice getFeignClientAdviceAnnotation(Method m) {
        FeignClientAdvice result = m.getAnnotation(FeignClientAdvice.class);
        if (Objects.isNull(result)) {
            result = m.getDeclaringClass().getAnnotation(FeignClientAdvice.class);
        }
        return result;
    }

    public Exception decode(String methodKey, Response response) {
        FeignClientExceptionHandler handler = exceptionHandlerMap.get(methodKey);
        if (Objects.nonNull(handler)) {
            return handler.handle(response);
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
