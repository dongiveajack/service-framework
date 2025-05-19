package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Anupam Dagar on 26/10/22
 */
@Data
public class RealmClientsVerifyResponse {
    private ResponseStatus status;
    private RealmClientsVerifyResponseData data;

    @Data
    public static class RealmClientsVerifyResponseData {
        @JsonProperty("isActive")
        private boolean isActive;

        private Integer id;

        private String name;

        @JsonProperty("clientId")
        private String clientId;

        @JsonProperty("clientSecret")
        private String clientSecret;
    }
}
