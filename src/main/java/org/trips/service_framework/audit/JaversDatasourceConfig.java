package org.trips.service_framework.audit;

/**
 * @author anomitra on 26/08/24
 */

import com.zaxxer.hikari.HikariDataSource;
import org.javers.repository.sql.ConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JaversDatasourceConfig {

    @Value("${javers.datasource.url}")
    private String url;

    @Value("${javers.datasource.username}")
    private String username;

    @Value("${javers.datasource.password}")
    private String password;

    @Value("${javers.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${javers.datasource.hikari.minimum-idle}")
    private String minIdleConns;

    @Value("${javers.datasource.hikari.maximum-pool-size}")
    private String maxPoolSize;

    @Bean(name = "JpaHibernateConnectionProvider")
    @Primary
    public ConnectionProvider jpaConnectionProvider() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(url);
        dataSourceProperties.setUsername(username);
        dataSourceProperties.setPassword(password);
        dataSourceProperties.setDriverClassName(driverClassName);
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        dataSource.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        dataSource.setMinimumIdle(Integer.parseInt(minIdleConns));
        return new JaversConnectionProvider(dataSource);
    }
}