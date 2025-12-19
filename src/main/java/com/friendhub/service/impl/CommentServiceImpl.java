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
import com.friendhub.repository.PostRepository;
import com.friendhub.repository.UserRepository;
import com.friendhub.service.CommentService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(long postId,
                                         CommentCreationRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(CurrentUser.id())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentMapper.toComment(request);
        comment.setPost(post);
        comment.setUser(user);



        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getAllCommentsByPostId(long postId) {
        return commentRepository.findAllByPostId(postId).stream()
                .map(commentMapper::toCommentResponse).toList();
    }


}
