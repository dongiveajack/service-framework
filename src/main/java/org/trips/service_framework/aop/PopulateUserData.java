package org.trips.service_framework.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : hardikphalet
 * @mailto : hardik.phalet@captainfresh.in (@gmail.com)
 * @created : 15/06/23, Thursday
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PopulateUserData {
}
