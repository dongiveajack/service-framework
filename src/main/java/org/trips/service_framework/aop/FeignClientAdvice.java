package org.trips.service_framework.aop;

import org.trips.service_framework.exceptions.annotations.FeignClientExceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignClientAdvice {
    Class<? extends FeignClientExceptionHandler> value();
}
