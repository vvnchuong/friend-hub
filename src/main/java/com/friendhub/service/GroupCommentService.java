package com.friendhub.service;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.dto.response.CursorResponse;

public interface GroupCommentService {

    CommentResponse createCommentInGroup(long groupId, long postId, CommentCreationRequest request);

    CursorResponse<CommentResponse> getCommentsGroup(long groupId, long postId, Long lastId);

}
