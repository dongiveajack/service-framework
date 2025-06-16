package org.trips.service_framework.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.List;

/**
 * @author anomitra on 13/06/25
 */

public class FaasDataSourceProperties extends DataSourceProperties {
    @Getter
    @Setter
    private List<String> namespaces;
    @Getter
    @Setter
    private Integer minimumIdle;
    @Getter
    @Setter
    private Integer maximumPoolSize;
}
