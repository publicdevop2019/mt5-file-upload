package com.hw.shared.sql.builder;

import com.hw.shared.sql.clause.WhereClause;
import com.hw.shared.sql.exception.EmptyQueryValueException;
import com.hw.shared.sql.exception.EmptyWhereClauseException;
import com.hw.shared.sql.exception.UnknownWhereClauseException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

public class PredicateConfig<T> {
    protected boolean allowEmptyClause = false;
    protected Set<WhereClause<T>> defaultWhereField = new HashSet<>();
    protected Map<String, WhereClause<T>> supportedWhereField = new HashMap<>();

    protected Predicate getPredicate(String search, CriteriaBuilder cb, Root<T> root) {
        List<Predicate> results = new ArrayList<>();
        if (search == null) {
            if (!allowEmptyClause)
                throw new EmptyWhereClauseException();
        } else {
            String[] queryParams = search.split(",");
            for (String param : queryParams) {
                String[] split = param.split(":");
                if (split.length == 2) {
                    if (supportedWhereField.get(split[0]) == null)
                        throw new UnknownWhereClauseException();
                    if (supportedWhereField.get(split[0]) != null && !split[1].isBlank()) {
                        WhereClause<T> tWhereClause = supportedWhereField.get(split[0]);
                        Predicate whereClause = tWhereClause.getWhereClause(split[1], cb, root);
                        results.add(whereClause);
                    }
                } else {
                    throw new EmptyQueryValueException();
                }
            }
        }
        if (defaultWhereField.size() != 0) {
            Set<Predicate> collect = defaultWhereField.stream().map(e -> e.getWhereClause(null, cb, root)).collect(Collectors.toSet());
            results.addAll(collect);
        }
        return cb.and(results.toArray(new Predicate[0]));
    }
}
