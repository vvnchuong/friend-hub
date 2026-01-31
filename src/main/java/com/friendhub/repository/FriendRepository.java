package com.friendhub.repository;

import com.friendhub.entity.Friend;
import com.friendhub.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByUserLowIdAndUserHighIdAndStatus(
            long userLowId,
            long userHighId,
            FriendStatus status);

    @Query(value = "SELECT f.* " +
            "FROM friends f " +
            "JOIN users u " +
            "ON ((f.user_low_id = :userId AND u.id = f.user_high_id) " +
            "OR (f.user_high_id = :userId AND u.id = f.user_low_id)) " +
            "AND u.status = 'ACTIVE' " +
            "WHERE (:lastId IS NULL OR f.id < :lastId) " +
            "AND f.status = :status " +
            "AND (f.user_low_id = :userId OR f.user_high_id = :userId) " +
            "ORDER BY f.id DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Friend> findFriends(@Param("userId") long userId,
                             @Param("status") String status,
                             @Param("lastId") Long lastId,
                             @Param("limit") int limit);

    @Query(value = "SELECT f.* " +
            "FROM friends f " +
            "JOIN users u " +
            "ON u.id = IF (f.user_low_id = :userId, " +
            " f.user_high_id, " +
            "f.user_low_id) " +
            "AND u.status = 'ACTIVE' " +
            "WHERE (:lastId  IS NULL OR f.id < :lastId) " +
            "AND f.status = 'PENDING' " +
            "AND f.requester_id <> :userId " +
            "AND (f.user_low_id = :userId OR f.user_high_id = :userId) " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Friend> findPendingRequests(@Param("userId") long userId,
                                     @Param("lastId") Long lastId,
                                     @Param("limit") int limit);

    Optional<Friend> findByUserLowIdAndUserHighId(long lowId, long highId);

}
