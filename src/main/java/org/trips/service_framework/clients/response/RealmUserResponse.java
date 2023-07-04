package org.trips.service_framework.clients.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author : hardikphalet
 * @mailto : hardik.phalet@captainfresh.in (@gmail.com)
 * @created : 15/06/23, Thursday
 **/

@Data
public class RealmUserResponse {
    private Status status;
    private Data data;

    public List<RealmUser> getWhiteListedUsers() {
        if (Objects.nonNull(data))
            return this.data.getWhitelistedUsers();
        return Collections.emptyList();
    }

    @lombok.Data
    public static class Status {
        private int code;
        private String message;
        private String type;
        @JsonProperty("totalCount")
        private int totalCount;
    }

    @lombok.Data
    public static class Data {
        @JsonProperty("whitelistedUsers")
        private List<RealmUser> whitelistedUsers;
        private int count;
    }
}
