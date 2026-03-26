package org.trips.service_framework.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.trips.service_framework.clients.request.NotificationRequest;

/**
 * contains the data required for the sending a sms notification.
 */
@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SMSNotificationRequest extends NotificationRequest {
    private Data data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String type;

        private String to;

        private String message;
    }
}
