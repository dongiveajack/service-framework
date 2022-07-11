package org.trips.service_framework.models.responses;

import org.trips.service_framework.codes.StatusCode;
import org.trips.service_framework.codes.SuccessCodes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By Abhinav Tripathi
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse implements Serializable {
    private Integer statusCode;
    private String statusMessage;
    private Type statusType;
    private Integer totalCount;

    public enum Type {
        ERROR,
        SUCCESS,
        WARNING
    }

    public StatusResponse(StatusCode statusCode, Type statusType, Integer totalCount) {
        this.statusCode = statusCode.getCode();
        this.statusMessage = statusCode.getMessage();
        this.statusType = statusType;
        if (statusCode instanceof SuccessCodes) {
            this.statusType = Type.SUCCESS;
        } else {
            this.statusType = Type.ERROR;
        }
        this.totalCount = totalCount;
    }

    public StatusResponse(StatusCode statusCode, Integer totalCount) {
        this.statusCode = statusCode.getCode();
        this.statusMessage = statusCode.getMessage();
        if (statusCode instanceof SuccessCodes) {
            this.statusType = Type.SUCCESS;
        } else {
            this.statusType = Type.ERROR;
        }
        this.totalCount = totalCount;
    }
}
