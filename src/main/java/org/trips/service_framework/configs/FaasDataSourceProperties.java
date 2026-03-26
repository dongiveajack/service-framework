package org.trips.service_framework.configs;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.List;

/**
 * @author anomitra on 13/06/25
 */

@Data
public class FaasDataSourceProperties extends DataSourceProperties {
    private List<String> namespaces;
    private Integer minimumIdle;
    private Integer maximumPoolSize;
    private Boolean flywayMigrate = Boolean.FALSE;
}
