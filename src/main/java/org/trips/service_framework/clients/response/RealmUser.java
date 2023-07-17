package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Created By anomitra on 25/05/23
 */
@With
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