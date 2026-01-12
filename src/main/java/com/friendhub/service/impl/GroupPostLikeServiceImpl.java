package com.friendhub.service.impl;

import com.friendhub.dto.response.PostLikeResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostLike;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.PostLikeRepository;
import com.friendhub.service.*;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupPostLikeServiceImpl implements GroupPostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final UserService userService;
    private final GroupMemberService groupMemberService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public PostLikeResponse likeOrUnlikePostInGroup(long groupId, long postId) {
        Post post = getPostInGroup(groupId, postId);
        User user = userService.getUserById(CurrentUser.id());

        if (!groupMemberService.isGroupMember(groupId, CurrentUser.id()))
            throw new AppException(ErrorCode.GROUP_MEMBER_NOT_FOUND);

        Optional<PostLike> existingLike = postLikeRepository
                .findByPostIdAndUserId(postId, user.getId());

        boolean isLiked;

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            isLiked = false;

            notificationService.removeUnseenLikeNotification(
                    user.getId(), post.getUser().getId());
        } else {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .createdAt(Instant.now())
                    .build();

            isLiked = true;
            postLikeRepository.save(postLike);

            if (!(user.getId() == post.getUser().getId()))
                notificationService.createLikeNotification(
                        user, post.getUser(), post);

        }

        int totalLikes = postLikeRepository.countByPostId(postId);

        return PostLikeResponse.builder()
                .postId(postId)
                .userId(user.getId())
                .totalLikes(totalLikes)
                .isLiked(isLiked)
                .build();
    }

    private Post getPostInGroup(long groupId, long postId) {
        Post post = postService.getPostById(postId);

        if (post.getGroup().getId() != groupId)
            throw new AppException(ErrorCode.POST_NOT_IN_GROUP);

        return post;
    }

}
