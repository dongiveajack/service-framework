package org.trips.service_framework.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.trips.service_framework.utils.FaasRoutingDataSource;

import javax.sql.DataSource;

/**
 * @author anomitra on 09/06/25
 */

@Component
@RequiredArgsConstructor
public class DataSourceConfig {
    @Bean
    @Primary
    public DataSource routingDataSource(DataSourcePropertiesConfig dataSourceConfig) {
        return FaasRoutingDataSource.of(dataSourceConfig.getDataSources());
    }
}
