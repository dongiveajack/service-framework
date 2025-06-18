package org.trips.service_framework.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.trips.service_framework.utils.RoutingDataSource;

import javax.sql.DataSource;

/**
 * @author anomitra on 09/06/25
 */

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    @Bean
    @Primary
    public DataSource routingDataSource(DataSourcePropertiesConfig dataSourceConfig) {
        return RoutingDataSource.of(dataSourceConfig.getDataSources());
    }
}
