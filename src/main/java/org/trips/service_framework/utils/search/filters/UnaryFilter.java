package org.trips.service_framework.utils.search.filters;

import lombok.Builder;
import lombok.Data;
import org.trips.service_framework.utils.search.Filter;
import org.trips.service_framework.utils.search.operators.UnaryOperator;

/**
 * @author : hardikphalet
 * @since : 22/07/24
 **/

@Data
@Builder
public class UnaryFilter implements Filter {
    private String field;
    private UnaryOperator operation;

    @Override
    public String getQuery() {
        return String.format("%s.%s", field, operation.getOperation());
    }
}
