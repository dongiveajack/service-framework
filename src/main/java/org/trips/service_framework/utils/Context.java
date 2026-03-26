package org.trips.service_framework.utils;

import lombok.Data;

/**
 * Created By Abhinav Tripathi on 2019-05-10
 */
@Data
public class Context {
    private final static ThreadLocal<String> namespaceId = new ThreadLocal<>();
    private final static ThreadLocal<String> userId = new ThreadLocal<>();

    public static String getNamespaceId() {
        return namespaceId.get();
    }

    public static void setNamespaceId(String id) {
        namespaceId.set(id);
    }

    public static void setUserId(String availUserId) {
        userId.set(availUserId);
    }

    public static String getUserId() {
        return userId.get();
    }

    public static void clean() {
        namespaceId.remove();
        userId.remove();
    }
}
