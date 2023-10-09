package org.trips.service_framework.clients.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * provides base request format from which all the notificationType requests are inherited.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String user;

    @JsonProperty("clientCode")
    private String clientCode;

    private String subject;
}
