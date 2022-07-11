package org.trips.service_framework.models;

import org.trips.service_framework.utils.SearchHelper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Abhinav Tripathi 27/10/20
 */
@Setter
@AllArgsConstructor
public class CustomSearchSpecification<T> implements Specification<T> {
    private String filters;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        filters = Objects.isNull(filters) ? StringUtils.EMPTY : filters;
        String[] orFilterItems = filters.split("__");
        List<Predicate> finalPredicates = new ArrayList<>();
        for (String filter : orFilterItems) {
            Map<SearchOperator, Map<String, String>> searchParams = SearchHelper.parseSearchParams(filter);
            Predicate[] predicates = SearchHelper.getPredicatesFromSearchParams(searchParams, root, criteriaBuilder);
            Predicate orPredicate = criteriaBuilder.and(predicates);
            finalPredicates.add(orPredicate);
        }
        Predicate[] predicates = finalPredicates.toArray(new Predicate[0]);
        query.where(criteriaBuilder.or(predicates));
        return null;
    }
}
