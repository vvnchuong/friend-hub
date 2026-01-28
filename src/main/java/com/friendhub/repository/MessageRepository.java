package com.friendhub.repository;

import com.friendhub.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Lấy tất cả messages giữa 2 users
    @Query(value = "SELECT m.* " +
            "FROM messages m " +
            "WHERE ((m.sender_id = :user1Id AND m.receiver_id = :user2Id) " +
            "OR (m.sender_id = :user2Id AND m.receiver_id = :user1Id)) " +
            "AND (:lastId IS NULL OR m.id > :lastId) " +
            "ORDER BY m.created_at ASC", nativeQuery = true)
    List<Message> findChatMessages(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id,
            @Param("lastId") Long lastId);

    // Đếm unread messages
    @Query("SELECT COUNT(m) FROM Message m WHERE " +
            "m.receiverId = :userId AND m.isRead = false")
    Long countUnreadMessages(Long userId);
}