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
            FriendStatus status
    );

    @Query("""
    SELECT f
    FROM Friend f
    WHERE f.status = :status
      AND (f.userLow.id = :userId OR f.userHigh.id = :userId)
""")
    List<Friend> findFriends(
            @Param("userId") long userId,
            @Param("status") FriendStatus status
    );

    @Query("""
    SELECT f
    FROM Friend f
    WHERE f.status = 'PENDING'
      AND f.requester.id <> :userId
      AND (f.userLow.id = :userId OR f.userHigh.id = :userId)
""")
    List<Friend> findPendingRequests(@Param("userId") long userId);

    Optional<Friend> findByUserLowIdAndUserHighId(long lowId, long highId);


}
