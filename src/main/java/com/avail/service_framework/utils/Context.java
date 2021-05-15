package com.avail.service_framework.utils;

import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 2019-05-10
 */
public class Context {
    private static ThreadLocal<Long> user;

    public static Long getContextInfo() {
        if (Objects.nonNull(user)) {
            return user.get();
        }
        return null;
    }

    public static void setContext(Long userId) {
        user = new ThreadLocal<>();
        user.set(userId);
    }
}
