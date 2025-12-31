package com.friendhub.service;

import com.friendhub.dto.response.NotificationResponse;
import com.friendhub.entity.Group;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;

import java.util.List;

public interface NotificationService {

    void createLikeNotification(User sender, User receiver, Post post);

    void createCommentNotification(User sender, User receiver, Post post);

    void removeUnseenLikeNotification(Long senderId, Long receiverId);

    void createGroupJoinApprovedNotification(User receiver, Group group, User handler);

    int countUnreadNotifications();

    List<NotificationResponse> getMyNotifications();

}
