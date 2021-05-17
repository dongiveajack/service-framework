package com.avail.service_framework.models.responses;

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
public class BaseResponse implements Serializable {
    private StatusResponse status;
}
