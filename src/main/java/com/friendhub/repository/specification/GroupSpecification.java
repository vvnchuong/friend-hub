package com.friendhub.repository.specification;

import com.friendhub.dto.request.GroupSearchRequest;
import com.friendhub.entity.Group;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GroupSpecification {

    public static Specification<Group> build(GroupSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String like = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), like),
                                cb.like(cb.lower(root.get("description")), like)
                        )
                );

            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
