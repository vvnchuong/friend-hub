package com.friendhub.service;

import com.friendhub.dto.response.PostLikeResponse;

public interface GroupPostLikeService {

    PostLikeResponse likeOrUnlikePostInGroup(long groupId, long postId);

}
