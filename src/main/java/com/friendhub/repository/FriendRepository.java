package com.friendhub.repository;

import com.friendhub.entity.Friend;
import com.friendhub.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByRequesterIdAndAddresseeIdAndStatus(
            long requesterId, long addresseeId, FriendStatus status);

    Optional<Friend> findByAddresseeIdAndRequesterIdAndStatus(
            long addresseeId, long requesterId, FriendStatus status);

    Optional<Friend> findByRequesterIdAndAddresseeIdAndStatus(
            long addresseeId, long requesterId, FriendStatus status);

    List<Friend> findByRequesterIdAndStatus(long userId, FriendStatus status);

    List<Friend> findByAddresseeIdAndStatus(long userId, FriendStatus status);

}
