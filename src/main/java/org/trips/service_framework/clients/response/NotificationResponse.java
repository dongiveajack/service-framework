package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class NotificationResponse {
    private ResponseStatus status;

    private JsonNode data;
}
