package com.friendhub.repository;

import com.friendhub.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c.* " +
            "FROM comments c " +
            "WHERE (:lastId  IS NULL OR c.id < :lastId) " +
            "AND c.post_id = :postId " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Comment> findCommentsPost(@Param("postId") long postId,
                                   @Param("lastId") Long lastId,
                                   @Param("limit") int limit);

    int countByPostId(long postId);

}
