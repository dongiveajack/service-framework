package org.trips.service_framework.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CmsSearchRequestBody {
    @NotEmpty(message = "Search Type is required")
    private String searchType;

    private List<String> skuCodes;
    private SkuAttributes attributes;
    private Boolean includeAttributes;
}
