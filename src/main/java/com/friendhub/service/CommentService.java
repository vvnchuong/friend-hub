package com.friendhub.service;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.dto.response.CursorResponse;

public interface CommentService {

    CommentResponse createComment(long postId, CommentCreationRequest request);

    CursorResponse<CommentResponse> getAllCommentsPost(long postId, Long lastId);

    void updateCommentPolicy(long postId, UpdateCommentPolicyRequest request);

}
