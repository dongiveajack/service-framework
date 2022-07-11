package org.trips.service_framework.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Created By Abhinav Tripathi
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
    private StatusResponse status;
}
