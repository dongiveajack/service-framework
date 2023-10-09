package org.trips.service_framework.notificationHandler.dtos;

import lombok.*;
import org.springframework.validation.annotation.Validated;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class SMSDto {
    private String notificationSubject;

    private String to;

    @Builder.Default
    private String type = "TXN";

    private String message;
}
