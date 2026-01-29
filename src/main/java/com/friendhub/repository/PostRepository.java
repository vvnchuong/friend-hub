package com.friendhub.repository;

import com.friendhub.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post> {

    List<Post> findAllByUserIdAndGroupId(long userId, long groupId);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "JOIN users u " +
            "ON p.user_id = u.id " +
            "WHERE u.id = :userId " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "AND p.group_id IS NULL " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Post> findAllPostsOfUser(@Param("userId") long userId,
                                  @Param("lastId") Long lastId,
                                  @Param("limit") long limit);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.user_id = :userId " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "AND p.group_id IS NULL " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Post> findAllMyPosts(@Param("userId") long userId,
                              @Param("lastId") Long lastId,
                              @Param("limit") int limit);

    @Query(value = "SELECT DISTINCT p.* " +
            "FROM posts p " +
            "JOIN users u " +
            "ON u.id = p.user_id " +
            "AND u.status = 'ACTIVE' " +
            "LEFT JOIN friends f " +
            "ON ( f.status = 'ACCEPTED' " +
            "AND f.user_low_id = LEAST(:userId, p.user_id) " +
            "AND f.user_high_id = GREATEST(:userId, p.user_id)) " +
            "WHERE (p.user_id = :userId OR f.id IS NOT NULL) " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "AND (p.user_id = :userId " +
            "OR p.privacy IN ('PUBLIC', 'FRIEND')) " +
            "AND p.group_id IS NULL " +
            "ORDER BY p.created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Post> findFeed(@Param("userId") long userId,
                        @Param("lastId") Long lastId,
                        @Param("limit") int limit);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "JOIN users u " +
            "ON u.id = p.user_id " +
            "AND u.status = 'ACTIVE' " +
            "JOIN groups g " +
            "ON p.group_id = g.id " +
            "JOIN users u " +
            "ON u.id = p.user_id " +
            "WHERE g.id = :groupId " +
            "AND u.status = 'ACTIVE' " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "ORDER BY p.id DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Post> findAllPostsInGroup(@Param("groupId") long groupId,
                                   @Param("lastId") Long lastId,
                                   @Param("limit") int limit);

    Optional<Post> findByIdAndGroupId(long postId, long groupId);

    Long countByUserId(long userId);

    Long countByGroupId(long groupId);

}
