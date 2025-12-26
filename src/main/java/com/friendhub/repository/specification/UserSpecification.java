package com.friendhub.repository.specification;

import com.friendhub.dto.request.AdminUserSearchRequest;
import com.friendhub.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> build(AdminUserSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String like = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("email")), like),
                                cb.like(cb.lower(root.get("firstName")), like),
                                cb.like(cb.lower(root.get("lastName")), like)
                        )
                );
            }

            if (request.getRole() != null) {
                predicates.add(
                        cb.equal(root.get("role").get("name"), request.getRole())
                );
            }

            if (request.getGender() != null) {
                predicates.add(
                        cb.equal(root.get("gender"), request.getGender())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

