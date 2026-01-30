package com.friendhub.service.impl;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.entity.Comment;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
import com.friendhub.enums.CommentPolicy;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.CommentMapper;
import com.friendhub.repository.CommentRepository;
import com.friendhub.service.*;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final FriendService friendService;
    private final NotificationService notificationService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(
            long postId,
            CommentCreationRequest request) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(CurrentUser.id());

        Comment comment = commentMapper.toComment(request);
        comment.setPost(post);
        comment.setUser(user);

        CommentPolicy policy = post.getCommentPolicy();

        switch (policy) {
            case OPEN -> {
            }
            case FRIEND_ONLY -> {
                if (!friendService.areFriends(
                        post.getUser().getId(),
                        CurrentUser.id()))
                    throw new AppException(ErrorCode.COMMENT_RESTRICTED_TO_FRIENDS);
            }
            case DISABLED -> throw new AppException(ErrorCode.COMMENT_DISABLED_BY_ADMIN);
        }

        if (!(user.getId() == post.getUser().getId()))
            notificationService
                    .createCommentNotification(user, post.getUser(), post);

        return commentMapper
                .toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<CommentResponse> getAllCommentsPost(long postId, Long lastId) {
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> commentRepository
                        .findCommentsPost(postId, lastId, pageSize + 1),
                Comment::getId,
                c -> c.stream()
                        .map(commentMapper::toCommentResponse)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void updateCommentPolicy(long postId, UpdateCommentPolicyRequest request) {
        Post post = postService.getPostById(postId);

        boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());

        if (!isAuthor)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (post.getCommentPolicy().equals(CommentPolicy.DISABLED))
            throw new AppException(ErrorCode.COMMENT_DISABLED_BY_ADMIN);

        post.setCommentPolicy(request.getPolicy());

        postService.updateCommentPolicy(post);
    }

}
