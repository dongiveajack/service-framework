package org.trips.service_framework.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author : hardikphalet
 * @mailto : hardik.phalet@captainfresh.in (@gmail.com)
 * @created : 17/06/23, Saturday
 **/
public class ReflectionUtils {
    @SneakyThrows
    public static Map<Field, Method> getAnnotatedFieldMethods(Class<?> clazz, List<Field> fields, MethodType methodType) {
        Map<Field, Method> result = Maps.newHashMap();
        for (Field field : fields) {
            Method getter = clazz.getMethod(getFunctionNameFromField(field.getName(), methodType));
            result.putIfAbsent(field, getter);
        }
        return result;
    }

    private static String getFunctionNameFromField(String fieldName, MethodType methodType) {
        switch (methodType) {
            case GETTER:
                return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
            case SETTER:
                return "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
            default:
                return fieldName;
        }
    }

    public static List<Object> castToList(Object obj) {
        List<Object> result = Lists.newArrayList();
        if (obj instanceof List) {
            result.addAll(((List<?>) obj));
        }
        return result;
    }

    public enum MethodType {
        GETTER,
        SETTER
    }
}
