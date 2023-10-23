package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Anupam Dagar on 26/10/22
 */
@Data
public class RealmSessionInfoResponse {
    private ResponseStatus status;
    private RealmSessionInfoResponseData data;

    @Data
    public static class RealmSessionInfoResponseData {
        @JsonProperty("userId")
        private String userId;

        private String name;
        private String email;

        @JsonProperty("phoneNumber")
        private String phoneNumber;

        @JsonProperty("sessionHandle")
        private String sessionHandle;

        @JsonProperty("supertokensUserId")
        private String supertokensUserId;
    }
}
