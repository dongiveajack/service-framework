package org.trips.service_framework.clients.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Abhinav Tripathi 15/05/25
 */
@Data
@AllArgsConstructor
public class RealmAuthenticateRequest {
    private List<String> clientIds;

    public static RealmAuthenticateRequest of(List<String> clientIds) {
        return new RealmAuthenticateRequest(clientIds);
    }
}
