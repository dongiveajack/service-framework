package org.trips.service_framework.aop.aspects;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.trips.service_framework.aop.UserField;
import org.trips.service_framework.clients.response.RealmUser;
import org.trips.service_framework.services.AuthService;
import org.trips.service_framework.utils.ReflectionUtils;
import org.trips.service_framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.trips.service_framework.utils.ReflectionUtils.MethodType.GETTER;


/**
 * @author : hardikphalet
 * @mailto : hardik.phalet@captainfresh.in (@gmail.com)
 * @created : 16/06/23, Friday
 **/

@Slf4j
@Aspect
@Component
@Configuration
@RequiredArgsConstructor
public class UserDataAspect {

    private final AuthService authService;
    @Value("${populate.user_data.enable:true}")
    private Boolean userDataActive;

    @SneakyThrows
    @Around("@annotation(org.trips.service_framework.aop.PopulateUserData)")
    public Object populateUserData(ProceedingJoinPoint joinPoint) {
        if (!userDataActive) {
            return joinPoint.proceed();
        }

        Object joinPointResult = joinPoint.proceed();

        Method getDataMethod = joinPointResult.getClass().getDeclaredMethod("getData");
        Object extractedData = getDataMethod.invoke(joinPointResult);

        List<Object> data = ReflectionUtils.castToList(extractedData);
        if (CollectionUtils.isEmpty(data)) {
            log.info("No data in response");
            return joinPoint.proceed();
        }

        Class<?> dataResultClass = data.get(0).getClass();
        List<Field> annotatedFields = FieldUtils.getFieldsListWithAnnotation(dataResultClass, UserField.class);

        if (CollectionUtils.isEmpty(annotatedFields)) {
            log.info("No annotated fields in the class");
            return joinPoint.proceed();
        }

        // If Getter is not found then ReflectionUtil will throw an error
        Map<Field, Method> annotatedFieldGetterMap = ReflectionUtils.getAnnotatedFieldMethods(dataResultClass, annotatedFields, GETTER);


        List<String> ids = StringUtils.getRealmIdsFromData(data, annotatedFieldGetterMap.values());

        Map<String, RealmUser> userInfoMap = authService.getUsers(ids);
        Method userInfoSetter = dataResultClass.getMethod("setUserInfo", Map.class);

        for (Object entity : data) {
            Map<String, RealmUser> usersMap = new HashMap<>();
            for (Field annotatedField : annotatedFields) {
                Method idFieldGetter = annotatedFieldGetterMap.get(annotatedField);
                String userId = (String) idFieldGetter.invoke(entity);
                if (Objects.isNull(userId) || userId.isEmpty()) {
                    break;
                }
                RealmUser userInfo = userInfoMap.get(userId)
                        .withFieldName(annotatedField.getName());
                usersMap.put(userId, userInfo);
            }
            userInfoSetter.invoke(entity, usersMap);
        }

        return joinPointResult;
    }

}
