package org.trips.service_framework.models;

import org.trips.service_framework.utils.SearchHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

/**
 * Created By Abhinav Tripathi
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SearchSpecification<T> implements Specification<T> {
    private Map<SearchOperator, Map<String, String>> searchParams;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Predicate[] predicates = SearchHelper.getPredicatesFromSearchParams(searchParams, root, builder);
        criteriaQuery.where(predicates);
        return null;
    }
}
