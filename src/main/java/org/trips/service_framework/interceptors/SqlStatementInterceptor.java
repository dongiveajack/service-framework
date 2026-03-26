package org.trips.service_framework.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.trips.service_framework.utils.Context;

/**
 * @author hardikphalet
 * @since 24/10/22, Mon
 */

@Slf4j
public class SqlStatementInterceptor implements StatementInspector {
    private static final String NAMESPACE_PLACEHOLDER = "${namespace-id}";

    @Override
    public String inspect(String sql) {
        if (sql.contains(NAMESPACE_PLACEHOLDER)) {
            return sql.replace(NAMESPACE_PLACEHOLDER, Context.getNamespaceId());
        }
        return sql;
    }
}
