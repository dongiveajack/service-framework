package org.trips.service_framework.utils.search.operators;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : hardikphalet
 * @since : 22/07/24
 **/

@Getter
@RequiredArgsConstructor
public enum UnaryOperator {
    IS_NULL("isNull"),
    IS_NOT_NULL("nn");

    private final String operation;
}
