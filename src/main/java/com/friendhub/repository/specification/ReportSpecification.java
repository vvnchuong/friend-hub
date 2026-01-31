package com.friendhub.repository.specification;

import com.friendhub.dto.request.ReportSearchRequest;
import com.friendhub.entity.Report;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    public static Specification<Report> build(ReportSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String like = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("reportReason")), like)
                        )
                );
            }

            if (request.getTargetType() != null)
                predicates.add(cb.equal(root.get("targetType"), request.getTargetType()));

            if (request.getStatus() != null)
                predicates.add(cb.equal(root.get("status"), request.getStatus()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
