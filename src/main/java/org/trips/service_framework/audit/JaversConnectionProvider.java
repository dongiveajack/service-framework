package org.trips.service_framework.audit;

import org.javers.repository.sql.ConnectionProvider;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author anomitra on 28/08/24
 */

public class JaversConnectionProvider implements ConnectionProvider {

    private final DataSource javersDataSource;

    public JaversConnectionProvider(DataSource javersDataSource) {
        this.javersDataSource = javersDataSource;
    }

    @Override
    public Connection getConnection() {
        return DataSourceUtils.getConnection(javersDataSource);
    }
}
