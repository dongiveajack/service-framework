package org.trips.service_framework.exceptions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Abhinav Tripathi 19/05/20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GrpcExceptionHandler {
    Class<? extends Throwable>[] value() default {};
}
