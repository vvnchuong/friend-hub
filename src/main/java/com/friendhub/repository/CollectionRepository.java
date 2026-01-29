package com.friendhub.repository;

import com.friendhub.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query(value = "SELECT c.* " +
            "FROM collections c " +
            "JOIN users u " +
            "ON c.user_id = u.id " +
            "WHERE c.user_id = :userId " +
            "AND (:lastId IS NULL OR c.id < :lastId) " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Collection> findAllCollectionByUserId(
            @Param("userId") long userId,
            @Param("lastId") Long lastId,
            @Param("limit") int limit);

    Optional<Collection> findByIdAndUserId(long collectionId, long userId);

    boolean existsByName(String name);

}
