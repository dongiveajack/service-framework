package org.trips.service_framework.audit;

/**
 * @author anomitra on 26/08/24
 */

import org.javers.repository.sql.ConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.trips.service_framework.configs.DataSourcePropertiesConfig;
import org.trips.service_framework.utils.RoutingDataSource;

import javax.sql.DataSource;

@Configuration
public class JaversDatasourceConfig {
    @Bean(name = "auditRoutingDataSource")
    public DataSource auditRoutingDataSource(DataSourcePropertiesConfig dataSourceConfig) {
        return RoutingDataSource.ofJavers(dataSourceConfig.getAuditDataSources());
    }

    @Bean(name = "JpaHibernateConnectionProvider")
    @Primary
    public ConnectionProvider jpaConnectionProvider (@Qualifier("auditRoutingDataSource") DataSource dataSource) {
        return new JaversConnectionProvider(dataSource);
    }
}