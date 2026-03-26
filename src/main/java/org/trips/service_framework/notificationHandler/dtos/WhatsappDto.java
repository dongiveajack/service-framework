package org.trips.service_framework.notificationHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * dto used for giving whatsapp notification data to service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappDto {
    private String notificationSubject;

    @Builder.Default
    private String provider = "whatsapp";

    private List<String> phoneNumbers;

    private String templateName;

    @Builder.Default
    private String languageCode = "en";

    private String mediaType;

    private String mediaUrl;

    private List<String> params;
}
