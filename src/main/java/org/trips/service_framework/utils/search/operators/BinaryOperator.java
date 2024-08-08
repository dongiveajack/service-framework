package org.trips.service_framework.utils.search.operators;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hardikphalet
 * @since : 22/07/24
 **/

@Getter
@RequiredArgsConstructor
public enum BinaryOperator {
    EQUAL_TO("eq"),
    NOT_EQUAL_TO("ne"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQUAL_TO("ge"),
    LESS_THAN("lt"),
    LESS_THAN_EQUAL_TO("le"),
    LIKE("like"),
    NOT_LIKE("nl"),
    IN("in"),
    NOT_IN("nin"),
    JSONB_PATH_EXISTS("jsonb_path_exists"),
    JSONB_PATH_EQUALS("jsonb_path_equals"),
    JSONB_PATH_CONTAINS("jsonb_path_contains");

    private final String operation;
}
