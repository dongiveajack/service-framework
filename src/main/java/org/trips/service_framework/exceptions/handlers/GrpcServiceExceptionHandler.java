package org.trips.service_framework.exceptions.handlers;

import org.trips.service_framework.exceptions.annotations.GrpcExceptionHandler;
import io.grpc.Status;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Abhinav Tripathi 19/05/20
 */
@Component
public class GrpcServiceExceptionHandler {
    private final AbstractApplicationContext applicationContext;

    public GrpcServiceExceptionHandler(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Status handleException(Exception e) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(BaseGrpcExceptionHandler.class);
        List<Object> collect = Stream.of(beanNamesForType).map(x -> applicationContext.getBeanFactory().getBean(x)).collect(Collectors.toList());
        for (Object bean : collect) {
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                GrpcExceptionHandler declaredAnnotation = method.getAnnotation(GrpcExceptionHandler.class);
                if (Objects.isNull(declaredAnnotation))
                    continue;
                Class<? extends Throwable>[] values = declaredAnnotation.value();
                for (Class clazz : values) {
                    if (clazz.isAssignableFrom(e.getClass())) {
                        if (method.getReturnType().isAssignableFrom(Status.class)) {
                            try {
                                return (Status) method.invoke(bean, e);
                            } catch (Exception e1) {
                                return Status.INTERNAL.withDescription("Error Occurred while executing method " + method.getName());
                            }
                        } else {
                            return Status.UNIMPLEMENTED.withDescription("Exception Handler Return type should be of io.grpc.Status for method " + clazz.getName());
                        }
                    }
                }
            }
        }
        return Status.UNKNOWN.withDescription("Unknown Error!! Please Contact Admin");
    }
}
