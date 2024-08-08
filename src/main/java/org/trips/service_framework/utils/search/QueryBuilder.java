package org.trips.service_framework.utils.search;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.trips.service_framework.utils.search.filters.BinaryFilter;
import org.trips.service_framework.utils.search.filters.UnaryFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A builder class for constructing query strings for the search API
 * <p>
 * This class allows for the combination of filters using logical {@code AND} and {@code OR} operators.
 * Filters are grouped by OR conditions, and within each group, filters are combined using AND conditions.
 * On the final {@code toQuery()} method, the filters are then converted to a search query string.
 * Individual operations are converted to their string representation by a method defined according to their {@code Filter} implementation.
 * Groups of filters separated by {@code OR} operator will be joined with {@code "__"},
 * and group of filters separated by {@code AND} operator will be joined with {@code ";"}
 * <p>
 * Usage example:
 * <pre>
 * {@code
 * String query = QueryBuilder.newQuery()
 *              .binaryFilter(BinaryFilter.builder()
 *                      .operator(BinaryOperator.EQ)
 *                      .field("...")
 *                      .value("...").build())
 *              .and().unaryFilter(UnaryFilter.builder()
 *                      .operation(UnaryOperator.IS_NULL)
 *                      .field("...")
 *                      .build())
 *              .or().unaryFilter(UnaryFilter.builder()
 *                      .operation(UnaryOperator.IS_NOT_NULL)
 *                      .field("...")
 *                      .build())
 *              .and().binaryFilter(BinaryFilter.builder()
 *                      .operator(BinaryOperator.IN)
 *                      .field("...")
 *                      .value("...").build())
 *              .toQuery();
 * }
 * </pre>
 * <p>
 * <b>Warnings:</b>
 * <ul>
 * <li>Ensure that every filter operation (unary or binary) is followed by an {@code and()} or {@code or()} operation to add the filter to the query.</li>
 * <li>If an {@code and()} or {@code or()} operation is not called after a filter, the filter will not be included in the final query.</li>
 * </ul>
 *
 * @author : hardikphalet
 * @since : 22/07/24
 */
public class QueryBuilder {
    private final List<List<Filter>> orSeparatedFilterList;
    private Filter currentFilter;

    private QueryBuilder() {
        orSeparatedFilterList = Lists.newArrayList();
        orSeparatedFilterList.add(Lists.newArrayList());
    }

    public static QueryBuilder newQuery() {
        return new QueryBuilder();
    }

    public QueryBuilder unaryFilter(UnaryFilter filter) {
        currentFilter = filter;
        return this;
    }

    public QueryBuilder binaryFilter(BinaryFilter<?> filter) {
        currentFilter = filter;
        return this;
    }

    public QueryBuilder and() {
        addCurrentFilter();
        return this;
    }

    public QueryBuilder or() {
        addCurrentFilter();
        orSeparatedFilterList.add(Lists.newArrayList());
        return this;
    }

    public String toQuery() {
        addCurrentFilter();
        return orSeparatedFilterList.stream()
                .map(andSeparatedFilters -> andSeparatedFilters.stream()
                        .map(Filter::getQuery)
                        .collect(Collectors.joining(";")))
                .collect(Collectors.joining("__"));
    }

    private void addCurrentFilter() {
        if (Objects.isNull(currentFilter)) {
            return;
        }
        Iterables.getLast(orSeparatedFilterList).add(currentFilter);
    }
}
