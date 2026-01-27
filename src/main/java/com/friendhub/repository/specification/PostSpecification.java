package com.friendhub.repository.specification;

import com.friendhub.dto.request.PostSearchRequest;
import com.friendhub.entity.Post;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    public static Specification<Post> build(PostSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String like = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("content")), like));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
