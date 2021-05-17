package com.avail.service_framework.utils;

import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 2019-05-10
 */
public class Context {
    private static ThreadLocal<String> user;

    public static String getContextInfo() {
        if (Objects.nonNull(user)) {
            return user.get();
        }
        return null;
    }

    public static void setContext(String userId) {
        user = new ThreadLocal<>();
        user.set(userId);
    }
}
