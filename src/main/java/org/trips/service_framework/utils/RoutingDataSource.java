package org.trips.service_framework.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.trips.service_framework.configs.FaasDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author anomitra on 11/06/25
 */

@Slf4j
public class RoutingDataSource {

    private AbstractRoutingDataSource getAbstractRoutingDataSource(List<FaasDataSourceProperties> propertiesList,
                                                                   boolean doMigrate) {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return Context.getNamespaceId();
            }
        };
        Map<Object, Object> targetDataSources = new HashMap<>();
        HikariDataSource defaultDataSource = null;
        for (var props : propertiesList) {
            HikariDataSource dataSource = props.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            dataSource.setMinimumIdle(props.getMinimumIdle());
            dataSource.setMaximumPoolSize(props.getMaximumPoolSize());
            for (var namespace : props.getNamespaces()) {
                targetDataSources.put(namespace, dataSource);
            }
            if (Objects.isNull(defaultDataSource)) {
                defaultDataSource = dataSource;
                log.info("Default data source is set to: {}", defaultDataSource.getJdbcUrl());
            }
            if (doMigrate && props.getFlywayMigrate()) {
                log.info("------------- EXECUTING FLYWAY MIGRATIONS -------------");
                Flyway.configure()
                        .dataSource(dataSource)
                        .baselineOnMigrate(true)
                        .locations("classpath:db/migration")
                        .load()
                        .migrate();
            }
        }
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    public static DataSource of(List<FaasDataSourceProperties> propertiesList) {
        return new RoutingDataSource().getAbstractRoutingDataSource(propertiesList, true);
    }

    public static DataSource ofJavers(List<FaasDataSourceProperties> propertiesList) {
        return new RoutingDataSource().getAbstractRoutingDataSource(propertiesList, false);
    }
}
