package org.trips.service_framework.notificationHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto used for giving sms notification data to service
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSDto {
    private String notificationSubject;

    private String to;

    @Builder.Default
    private String type = "TXN";

    private String message;
}
