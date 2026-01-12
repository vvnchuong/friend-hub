package com.friendhub.service.impl;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.entity.Comment;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.CommentMapper;
import com.friendhub.repository.CommentRepository;
import com.friendhub.service.*;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CommentResponse> getAllCommentsByGroupIdAndPostId(long groupId, long postId) {
        Post post = getPostInGroup(groupId, postId);

        return commentRepository.findAllByPostId(post.getId()).stream()
                .map(commentMapper::toCommentResponse).toList();
    }

    private Post getPostInGroup(long groupId, long postId) {
        Post post = postService.getPostById(postId);

        if (post.getGroup().getId() != groupId)
            throw new AppException(ErrorCode.POST_NOT_IN_GROUP);

        return post;
    }

}
