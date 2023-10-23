package org.trips.service_framework.aop;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author hardikphalet
 * @since 16/02/23, Thu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Client {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an auto-detected component.
     *
     * @return the suggested component name, if any (or empty String otherwise)
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

}
