package org.trips.service_framework.notificationHandler;

import org.trips.service_framework.clients.response.NotificationResponse;

public interface NotificationHandler {
    <T> NotificationResponse send(String subject,String clientCode, T data);
}
