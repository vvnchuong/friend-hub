package com.friendhub.service;

import com.friendhub.dto.response.PostLikeResponse;

public interface PostLikeService {

    PostLikeResponse likeOrUnLikePost(long postId);

}
