package org.trips.service_framework.clients.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest<T> {
    private String user;

    @JsonProperty("clientCode")
    private String clientCode;

    private String subject;

    private T data;
}
