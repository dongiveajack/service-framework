package org.trips.service_framework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.trips.service_framework.clients.request.NotificationRequest;

import java.util.List;

/**
 * contains the data required for the sending an email notification.
 */

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmailNotificationRequest extends NotificationRequest {
    private Data data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String sender;

        @JsonProperty("toAddresses")
        private List<String> toAddresses;

        private String body;

        private String subject;

        private String html;

        @JsonProperty("ccAddresses")
        private List<String> ccAddresses;

        @JsonProperty("bccAddresses")
        private List<String> bccAddresses;

        private String charset;
    }

}
