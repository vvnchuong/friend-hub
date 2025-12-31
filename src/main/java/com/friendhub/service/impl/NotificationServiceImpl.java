package com.friendhub.service.impl;

import com.friendhub.dto.response.NotificationResponse;
import com.friendhub.entity.Group;
import com.friendhub.entity.Notification;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
import com.friendhub.enums.NotificationType;
import com.friendhub.mapper.NotificationMapper;
import com.friendhub.repository.NotificationRepository;
import com.friendhub.service.NotificationService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void createLikeNotification(User sender, User receiver, Post post) {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .type(NotificationType.LIKE)
                .seen(false)
                .createdAt(Instant.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void createCommentNotification(User sender, User receiver, Post post) {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .type(NotificationType.COMMENT)
                .seen(false)
                .createdAt(Instant.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void removeUnseenLikeNotification(Long senderId, Long receiverId) {
        notificationRepository.deleteBySenderIdAndReceiverIdAndTypeAndSeen(
                senderId,
                receiverId,
                NotificationType.LIKE,
                false
        );
    }

    @Override
    public void createGroupJoinApprovedNotification(
            User receiver, Group group, User handler) {
        Notification notification = Notification.builder()
                .sender(handler)
                .receiver(receiver)
                .group(group)
                .type(NotificationType.GROUP_JOIN_APPROVED)
                .seen(false)
                .createdAt(Instant.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public int countUnreadNotifications() {
        return notificationRepository.countUnseenNotifications(CurrentUser.id());
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {
        notificationRepository.markAllAsSeen(CurrentUser.id());
        return notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(CurrentUser.id())
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }


}
