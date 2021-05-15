package com.avail.service_framework.models.responses;

import lombok.Data;

import java.io.Serializable;

/**
 * Created By Abhinav Tripathi
 */
@Data
public class BaseResponse implements Serializable {
    private StatusResponse status;
}
