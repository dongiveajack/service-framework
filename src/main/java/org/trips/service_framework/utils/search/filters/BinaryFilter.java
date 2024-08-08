package org.trips.service_framework.utils.search.filters;

import lombok.Builder;
import lombok.Data;
import org.trips.service_framework.utils.search.Filter;
import org.trips.service_framework.utils.search.operators.BinaryOperator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : hardikphalet
 * @since : 22/07/24
 **/
@Data
@Builder
public class BinaryFilter<T> implements Filter {
    private String field;
    private T value;
    private BinaryOperator operation;

    @Override
    public String getQuery() {
        Optional<String> formattedValue = Optional.empty();
        if (value instanceof List) {
            formattedValue = Optional.of(((List<?>) value).stream().map(Object::toString).collect(Collectors.joining(",")));
        }
        return String.format("%s.%s:%s", field, operation.getOperation(), formattedValue.orElse(value.toString()));
    }
}
