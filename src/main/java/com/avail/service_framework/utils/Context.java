package com.avail.service_framework.utils;

import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 2019-05-10
 */
public class Context {
    private final static ThreadLocal<String> userToken = new ThreadLocal<>();
    private final static ThreadLocal<String> userId = new ThreadLocal<>();

    public static String getToken() {
        return userToken.get();
    }

    public static String getUserId() {
        return userId.get();
    }

    public static void setUserToken(String token) {
        userToken.set(token);
    }

    public static void setUserId(String availUserId) {
        userId.set(availUserId);
    }
}
