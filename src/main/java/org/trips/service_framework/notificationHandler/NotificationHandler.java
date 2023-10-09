package org.trips.service_framework.notificationHandler;

import lombok.NonNull;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;

public interface NotificationHandler {
    <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest);
}
