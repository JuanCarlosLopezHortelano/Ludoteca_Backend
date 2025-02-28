package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrestamoSpecification implements Specification<Prestamo> {
    private static final long serialVersionUID = 1L;
    private final SearchCriteria criteria;

    public PrestamoSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }

        if ((criteria.getOperation().equalsIgnoreCase("<=") || criteria.getOperation().equalsIgnoreCase(">=")) && criteria.getValue() != null) {
            LocalDate filterDate = LocalDate.parse((String) criteria.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Path<LocalDate> datePath = root.get(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase("<=")) {
                return builder.lessThanOrEqualTo(datePath, filterDate);
            } else {
                return builder.greaterThanOrEqualTo(datePath, filterDate);
            }
        }
        return null;
    }

    private Path<String> getPath(Root<Prestamo> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }
        return expression;
    }
}
