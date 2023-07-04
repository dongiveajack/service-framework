package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By anomitra on 25/05/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealmUser {
    private String id;
    private String name;
    private String email;
    @JsonProperty("phoneNumber")
    public String phoneNumber;
    @JsonProperty("isActive")
    public boolean isActive;
}