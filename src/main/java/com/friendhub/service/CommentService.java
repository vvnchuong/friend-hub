package com.friendhub.service;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(long postId, CommentCreationRequest request);

    List<CommentResponse> getAllCommentsByPostId(long postId);

}
