package com.friendhub.service.impl;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.entity.Comment;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
import com.friendhub.mapper.CommentMapper;
import com.friendhub.repository.CommentRepository;
import com.friendhub.service.CommentService;
import com.friendhub.service.NotificationService;
import com.friendhub.service.PostService;
import com.friendhub.service.UserService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(
            long postId,
            CommentCreationRequest request) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(CurrentUser.id());

        Comment comment = commentMapper.toComment(request);
        comment.setPost(post);
        comment.setUser(user);

        notificationService
                .createCommentNotification(user, post.getUser(), post);

        return commentMapper
                .toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getAllCommentsByPostId(long postId) {
        return commentRepository.findAllByPostId(postId).stream()
                .map(commentMapper::toCommentResponse).toList();
    }


}
