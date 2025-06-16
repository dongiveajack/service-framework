package org.trips.service_framework.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author anomitra on 13/06/25
 */

@Component
@ConfigurationProperties
@Data
public class DataSourcePropertiesConfig {
    private List<FaasDataSourceProperties> dataSources;
    private List<FaasDataSourceProperties> auditDataSources;
}
