package org.trips.service_framework.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.trips.service_framework.clients.response.RealmUser;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;

/**
 * Created By Abhinav Tripathi
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {
    private StatusResponse status;
    private Map<String, RealmUser> userInfo;
}
