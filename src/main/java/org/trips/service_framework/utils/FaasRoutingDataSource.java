package org.trips.service_framework.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.trips.service_framework.configs.DataSourcePropertiesConfig;
import org.trips.service_framework.configs.FaasDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author anomitra on 11/06/25
 */

public class FaasRoutingDataSource {

    private AbstractRoutingDataSource getAbstractRoutingDataSource(
            List<FaasDataSourceProperties> propertiesList,
            boolean doMigrate
    ) {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return Context.getNamespaceId();
            }
        };
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (var props: propertiesList){
            HikariDataSource dataSource = props.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            dataSource.setMinimumIdle(props.getMinimumIdle());
            dataSource.setMaximumPoolSize(props.getMaximumPoolSize());
            for (var namespace: props.getNamespaces()) {
                targetDataSources.put(namespace, dataSource);
            }
            if (Objects.isNull(routingDataSource.getResolvedDefaultDataSource())) {
                routingDataSource.setDefaultTargetDataSource(dataSource);
            }
            if (doMigrate) {
                Flyway.configure()
                        .dataSource(dataSource)
                        .locations("classpath:db/migration")
                        .load()
                        .migrate();
            }
        }
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    public static DataSource of(List<FaasDataSourceProperties> propertiesList) {
        return new FaasRoutingDataSource().getAbstractRoutingDataSource(propertiesList, true);
    }

    public static DataSource ofJavers(List<FaasDataSourceProperties> propertiesList) {
        return new FaasRoutingDataSource().getAbstractRoutingDataSource(propertiesList, false);
    }
}
