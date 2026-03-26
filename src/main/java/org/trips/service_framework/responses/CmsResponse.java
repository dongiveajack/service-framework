package org.trips.service_framework.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.trips.service_framework.dtos.CmsSkuResponse.Sku;
import org.trips.service_framework.models.responses.BaseResponse;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CmsResponse extends BaseResponse {
    private List<Sku> data;
}