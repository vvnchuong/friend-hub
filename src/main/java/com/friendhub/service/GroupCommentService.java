package com.friendhub.service;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;

import java.util.List;

public interface GroupCommentService {

    CommentResponse createCommentInGroup(long groupId, long postId, CommentCreationRequest request);

    List<CommentResponse> getAllCommentsByGroupIdAndPostId(long groupId, long postId);

}
