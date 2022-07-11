package org.trips.service_framework.models.entries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created By Abhinav Tripathi on 2019-10-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchEntry {
    @NotNull
    private String filters;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer fetchSize = 1000;

    private String sortBy;

    private String includes;
}
