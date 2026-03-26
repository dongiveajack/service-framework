package org.trips.service_framework.notificationHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * dto used for giving email notification data to service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String notificationSubject;

    private String sender;

    private List<String> toAddresses;

    private String body;

    private String subject;

    private String html;

    private List<String> ccAddresses;

    private List<String> bccAddresses;

    @Builder.Default
    private String charset = "UTF-8";

}
