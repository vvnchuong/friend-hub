package com.friendhub.service.impl;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.entity.Comment;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
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

@Service
@RequiredArgsConstructor
public class GroupCommentServiceImpl implements GroupCommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final GroupMemberService groupMemberService;
    private final NotificationService notificationService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createCommentInGroup(long groupId, long postId, CommentCreationRequest request) {
        Post post = getPostInGroup(groupId, postId);
        User user = userService.getUserById(CurrentUser.id());

        if (!groupMemberService.isGroupMember(groupId, CurrentUser.id()))
                throw new AppException(ErrorCode.GROUP_MEMBER_NOT_FOUND);

        Comment comment = commentMapper.toComment(request);
        comment.setPost(post);
        comment.setUser(user);

        notificationService.createCommentNotification(user, post.getUser(), post);

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<CommentResponse> getCommentsGroup(long groupId, long postId, Long lastId) {
        Post post = getPostInGroup(groupId, postId);

        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> commentRepository
                        .findCommentsPost(post.getId(), lastId, pageSize + 1),
                Comment::getId,
                c -> c.stream()
                        .map(commentMapper::toCommentResponse)
                        .toList()
        );
    }

    private Post getPostInGroup(long groupId, long postId) {
        Post post = postService.getPostById(postId);

        if (post.getGroup().getId() != groupId)
            throw new AppException(ErrorCode.POST_NOT_IN_GROUP);

        return post;
    }

}
