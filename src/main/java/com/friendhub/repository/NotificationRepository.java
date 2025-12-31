package com.friendhub.repository;

import com.friendhub.entity.Notification;
import com.friendhub.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(long userId);

    void deleteBySenderIdAndReceiverIdAndTypeAndSeen(long sendId, long receiverId, NotificationType type, Boolean seen);

    @Query(value = "SELECT COUNT(*) " +
            "FROM notifications n " +
            "WHERE n.receiver_id = :receiverId " +
            "AND n.seen = 0 ", nativeQuery = true)
    int countUnseenNotifications(@Param("receiverId") long receiverId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE notifications n " +
            "SET n.seen = 1 " +
            "WHERE n.receiver_id = :receiverId " +
            "AND n.seen = 0", nativeQuery = true)
    void markAllAsSeen(@Param("receiverId") long receiverId);

}
