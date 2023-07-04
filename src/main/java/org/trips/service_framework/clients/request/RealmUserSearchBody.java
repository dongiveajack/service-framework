package org.trips.service_framework.clients.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author : hardikphalet
 * @mailto : hardik.phalet@captainfresh.in (@gmail.com)
 * @created : 15/06/23, Thursday
 **/

@Data
@AllArgsConstructor
public class RealmUserSearchBody {
    @JsonProperty
    public String email;
    @JsonProperty("ids")
    public Set<String> ids;
    @JsonProperty("fetchSize")
    public int fetchSize;
    @JsonProperty("page")
    public int page;
}
