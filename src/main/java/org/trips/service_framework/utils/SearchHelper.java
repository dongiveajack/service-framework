package org.trips.service_framework.utils;

import org.trips.service_framework.exceptions.ServiceException;
import org.trips.service_framework.models.SearchOperator;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By Abhinav Tripathi
 */
@Slf4j
public class SearchHelper {
    private static final String SEARCH_DELIMITER = ";";
    private static final String KEY_VALUE_DELIMITER = ":";
    private static final String OPERATOR_DELIMITER = "\\.";
    private static final String IN_VALUES_DELIMITER = ",";
    private static final String KEY_DELIMITER = "-";
    private static final String JSONB_OPERATOR_AND_DELIMITER = "&&";
    private static final String JSONB_OPERATOR_KEY_VALUE_DELIMITER = "\\|";

    /**
     * Note on JSON Query Related Params (jsonPathParams, jsonPathExists)
     * jsonPathParams is a tricky implementation as we need three inputs :
     * 1. Database Column
     * 2. Path
     * 3. Value
     * We intend to wrap path and Value in one single entry ( Value ).
     */
    @NotNull
    public static Map<SearchOperator, Map<String, String>> parseSearchParams(String filters) throws ServiceException {
        Map<SearchOperator, Map<String, String>> params = new HashMap<>();
        Map<String, String> inParams = new HashMap<>();
        Map<String, String> notInParams = new HashMap<>();
        Map<String, String> eqParams = new HashMap<>();
        Map<String, String> neParams = new HashMap<>();
        Map<String, String> isNullParams = new HashMap<>();
        Map<String, String> isNotNullParams = new HashMap<>();
        Map<String, String> gtParams = new HashMap<>();
        Map<String, String> ltParams = new HashMap<>();
        Map<String, String> leParams = new HashMap<>();
        Map<String, String> geParams = new HashMap<>();
        Map<String, String> likeParams = new HashMap<>();
        Map<String, String> nlParams = new HashMap<>();


        Map<String, String> jsonbPathParams = new HashMap<>();
        Map<String, String> jsonbPathExists = new HashMap<>();
        Map<String, String> jsonbPathContains = new HashMap<>();


        if (StringUtils.isEmpty(filters)) {
            return params;
        } else {
            String[] filterArray = filters.split(SEARCH_DELIMITER);
            for (String filter : filterArray) {
                try {
                    // Skip over the filter if it's empty
                    if (filter.isEmpty()){
                        continue;
                    }
                    String key, value;
                    int index = filter.indexOf(KEY_VALUE_DELIMITER);
                    if (index == -1) {
                        key = filter;
                        value = null;
                    } else {
                        String[] splitFilter = filter.split(KEY_VALUE_DELIMITER, 2);
                        value = splitFilter[1];
                        key = splitFilter[0];
                    }
                    String[] keyAndOperator = key.split(OPERATOR_DELIMITER);
                    String keyName = keyAndOperator[0];
                    String operator = keyAndOperator[1];
                    SearchOperator searchOperator = SearchOperator.value(operator);
                    if (Objects.isNull(searchOperator)) {
                        throw new ServiceException(String.format("%s is not a Valid Search Operator", operator));
                    }
                    switch (searchOperator) {
                        case IN:
                            inParams.put(keyName, value);
                            break;
                        case NOT_IN:
                            notInParams.put(keyName, value);
                            break;
                        case EQUAL_TO:
                            eqParams.put(keyName, value);
                            break;
                        case IS_NULL:
                            isNullParams.put(keyName, value);
                            break;
                        case IS_NOT_NULL:
                            isNotNullParams.put(keyName, value);
                            break;
                        case NOT_EQUAL_TO:
                            neParams.put(keyName, value);
                            break;
                        case GREATER_THAN:
                            gtParams.put(keyName, value);
                            break;
                        case LESS_THAN:
                            ltParams.put(keyName, value);
                            break;
                        case LESS_THAN_EQUAL_TO:
                            leParams.put(keyName, value);
                            break;
                        case GREATER_THAN_EQUAL_TO:
                            geParams.put(keyName, value);
                            break;
                        case LIKE:
                            likeParams.put(keyName, value);
                            break;
                        case NOT_LIKE:
                            nlParams.put(keyName, value);
                            break;
                        case JSONB_PATH_EQUALS:
                            jsonbPathParams.put(keyName, value);
                            break;
                        case JSONB_PATH_EXISTS:
                            jsonbPathExists.put(keyName, value);
                            break;
                        case JSONB_PATH_CONTAINS:
                            jsonbPathContains.put(keyName, value);
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new ServiceException("Search Parameters are not in defined format");
                }
            }
            params.put(SearchOperator.IN, inParams);
            params.put(SearchOperator.NOT_IN, notInParams);
            params.put(SearchOperator.EQUAL_TO, eqParams);
            params.put(SearchOperator.IS_NULL, isNullParams);
            params.put(SearchOperator.IS_NOT_NULL, isNotNullParams);
            params.put(SearchOperator.NOT_EQUAL_TO, neParams);
            params.put(SearchOperator.GREATER_THAN, gtParams);
            params.put(SearchOperator.LESS_THAN, ltParams);
            params.put(SearchOperator.LESS_THAN_EQUAL_TO, leParams);
            params.put(SearchOperator.GREATER_THAN_EQUAL_TO, geParams);
            params.put(SearchOperator.LIKE, likeParams);
            params.put(SearchOperator.NOT_LIKE, nlParams);
            params.put(SearchOperator.JSONB_PATH_EQUALS, jsonbPathParams);
            params.put(SearchOperator.JSONB_PATH_EXISTS, jsonbPathExists);
            params.put(SearchOperator.JSONB_PATH_CONTAINS, jsonbPathContains);

        }
        return params;
    }

    public static Predicate[] getPredicatesFromSearchParams(Map<SearchOperator, Map<String, String>> searchParams, Root root, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<>();
        for (Map.Entry<SearchOperator, Map<String, String>> entry : searchParams.entrySet()) {
            for (Map.Entry<String, String> e : entry.getValue().entrySet()) {
                addPredicate(entry.getKey(), e.getKey(), e.getValue(), root, predicateList, builder);
            }
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        return predicateList.toArray(predicates);
    }

    private static <T extends Enum<T>> Collection<T> getFilterEnum(Class<T> enumClass, Set<String> filterVal) {
        List<T> result = new ArrayList<>();
        if (!filterVal.isEmpty()) {
            result = filterVal.stream().map(value -> Enum.valueOf(enumClass, value)).collect(Collectors.toList());
        }
        return result;
    }

    private static void addPredicate(SearchOperator searchOperator, String key, String value, Root root, List<Predicate> predicates, CriteriaBuilder builder) {

        Path path = getPath(key, root);
        List<Expression<String>> expressionList = new ArrayList<>();

        switch (searchOperator) {
            case IN:
                Set<String> values = new HashSet<>(Arrays.asList(value.split(IN_VALUES_DELIMITER)));
                if (path.getJavaType().isEnum()) {
                    Collection<?> filterValuesList = getFilterEnum(path.getJavaType(), values);
                    predicates.add(path.in(filterValuesList));
                } else {
                    predicates.add(path.in(values));
                }
                break;
            case NOT_IN:
                Set<String> values1 = new HashSet<>(Arrays.asList(value.split(IN_VALUES_DELIMITER)));
                if (path.getJavaType().isEnum()) {
                    Collection<?> filterValuesList = getFilterEnum(path.getJavaType(), values1);
                    predicates.add(builder.not(path.in(filterValuesList)));
                } else {
                    predicates.add(builder.not(path.in(values1)));
                }
                break;
            case EQUAL_TO:
                if (path.getJavaType().isEnum()) {
                    predicates.add(path.in(Enum.valueOf(path.getJavaType(), value)));
                } else if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.equal((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.equal((Expression) path, parseDate(value)));
                } else if (Boolean.class.isAssignableFrom(path.getJavaType())) {
                    value = value.equals("1") ? "true" : "false";
                    predicates.add(builder.equal(path, Boolean.parseBoolean(value)));
                } else {
                    predicates.add(builder.equal(path, value));
                }
                break;
            case NOT_EQUAL_TO:
                if (path.getJavaType().isEnum()) {
                    predicates.add(builder.notEqual(path, Enum.valueOf(path.getJavaType(), value)));
                } else if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.notEqual((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.notEqual((Expression) path, parseDate(value)));
                } else if (Boolean.class.isAssignableFrom(path.getJavaType())) {
                    value = value.equals("1") ? "true" : "false";
                    predicates.add(builder.notEqual(path, Boolean.parseBoolean(value)));
                } else {
                    predicates.add(builder.notEqual(path, value));
                }
                break;
            case IS_NULL:
                predicates.add(builder.isNull(path));
                break;
            case IS_NOT_NULL:
                predicates.add(builder.isNotNull(path));
                break;
            case GREATER_THAN:
                if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.greaterThan((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.greaterThan((Expression) path, parseDate(value)));
                } else {
                    predicates.add(builder.greaterThan((Expression<String>) path, value));
                }
                break;
            case LESS_THAN:
                if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.lessThan((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.lessThan((Expression) path, parseDate(value)));
                } else {
                    predicates.add(builder.lessThan((Expression<String>) path, value));
                }
                break;
            case GREATER_THAN_EQUAL_TO:
                if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.greaterThanOrEqualTo((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.greaterThanOrEqualTo((Expression) path, parseDate(value)));
                } else {
                    predicates.add(builder.greaterThanOrEqualTo((Expression<String>) path, value));
                }
                break;
            case LESS_THAN_EQUAL_TO:
                if (Date.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.lessThanOrEqualTo((Expression) path, parseDate(value).toDate()));
                } else if (DateTime.class.isAssignableFrom(path.getJavaType())) {
                    predicates.add(builder.lessThanOrEqualTo((Expression) path, parseDate(value)));
                } else {
                    predicates.add(builder.lessThanOrEqualTo((Expression<String>) path, value));
                }
                break;
            case LIKE:
                if (path.getJavaType().isEnum()) {
                    predicates.add(builder.equal((Expression<String>) path, Enum.valueOf(path.getJavaType(), value)));
                } else if (Boolean.class.isAssignableFrom(path.getJavaType())) {
                    value = value.equals("1") ? "true" : "false";
                    predicates.add(builder.equal(path, Boolean.parseBoolean(value)));
                } else {
                    Expression<String> lowerField = builder.lower((Expression<String>) path);
                    predicates.add(builder.like(lowerField, value.toLowerCase() + "%"));
                }
                break;
            case NOT_LIKE:
                if (path.getJavaType().isEnum()) {
                    predicates.add(builder.equal((Expression<String>) path, Enum.valueOf(path.getJavaType(), value)));
                } else if (Boolean.class.isAssignableFrom(path.getJavaType())) {
                    value = value.equals("1") ? "false" : "true";
                    predicates.add(builder.equal(path, Boolean.parseBoolean(value)));
                } else {
                    predicates.add(builder.notLike((Expression<String>) path, value + "%"));
                }
                break;
            case JSONB_PATH_EXISTS:
                for (String filter : value.split(JSONB_OPERATOR_AND_DELIMITER)) {
                    expressionList.clear();
                    expressionList.add(path);
                    for (String jsonToken : filter.split(IN_VALUES_DELIMITER)) {
                        expressionList.add(builder.literal(jsonToken));
                    }
                    predicates.add(builder.isNotNull(builder.function("jsonb_extract_path_text", String.class, expressionList.toArray(new Expression[0]))));
                }
                break;

            case JSONB_PATH_EQUALS:
                for (String filter : value.split(JSONB_OPERATOR_AND_DELIMITER)) {
                    expressionList.clear();
                    expressionList.add(path);
                    for (String jsonToken : filter.split(JSONB_OPERATOR_KEY_VALUE_DELIMITER)[0].split(IN_VALUES_DELIMITER)) {
                        expressionList.add(builder.literal(jsonToken));
                    }
                    String[] valuesTemp = filter.split(JSONB_OPERATOR_KEY_VALUE_DELIMITER)[1].split(IN_VALUES_DELIMITER);
                    predicates.add(builder.function("jsonb_extract_path_text", String.class, expressionList.toArray(new Expression[0])).in((Object[]) valuesTemp));
                }
                break;
            case JSONB_PATH_CONTAINS:
                for (String filter : value.split(JSONB_OPERATOR_AND_DELIMITER)) {
                    expressionList.clear();
                    expressionList.add(path);
                    for (String jsonToken : filter.split(JSONB_OPERATOR_KEY_VALUE_DELIMITER)[0].split(IN_VALUES_DELIMITER)) {
                        expressionList.add(builder.literal(jsonToken));
                    }
                    predicates.add(builder.like(builder.function("jsonb_extract_path_text", String.class, expressionList.toArray(new Expression[0])), "%" + filter.split(JSONB_OPERATOR_KEY_VALUE_DELIMITER)[1] + "%"));
                }
                break;
        }
    }

    private static Path getPath(String key, Root root) {
        Path path = root;
        int index = key.indexOf(KEY_DELIMITER);
        if (index > -1) {
            String[] data = key.split(KEY_DELIMITER);
            Join innerJoin = root.join(data[0]);
            for (int i = 1; i < data.length - 1; i++) {
                innerJoin = innerJoin.join(data[i]);
            }
            path = innerJoin.get(data[data.length - 1]);
        } else {
            path = path.get(key);
        }
        return path;
    }

    public static Pageable getPageRequest(Integer page, Integer fetchSize, String sortBy) {
        Sort sort = null;
        if (!StringUtils.isEmpty(sortBy)) {
            String[] sortParams = sortBy.split(IN_VALUES_DELIMITER);
            for (String sortParam : sortParams) {
                String key;
                Sort.Direction value;
                int index = sortParam.indexOf(KEY_VALUE_DELIMITER);
                if (index == -1) {
                    key = sortParam;
                    value = Sort.Direction.ASC;
                } else {
                    String[] sortBySortOrder = sortParam.split(KEY_VALUE_DELIMITER);
                    key = sortBySortOrder[0];
                    value = Sort.Direction.fromString(sortBySortOrder[1]);
                }
                if (Objects.isNull(sort)) {
                    sort = Sort.by(value, key);
                } else {
                    sort = sort.and(Sort.by(value, key));
                }
            }
        }
        if (Objects.nonNull(sort))
            return PageRequest.of(page, fetchSize, sort);
        return PageRequest.of(page, fetchSize);

    }

    private static DateTime parseDate(String value) {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(value);
    }
}
