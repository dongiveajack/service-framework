package org.trips.service_framework.notificationHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
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
