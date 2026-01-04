package com.friendhub.service;

import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserRole;
import com.friendhub.exception.AppException;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostService postService;
    private final PostQueryService postQueryService;

    @Transactional(readOnly = true)
    public PostResponse getPostDetail(long postId) {
        Post post = postService.getPostById(postId);

        return postQueryService.build(post, CurrentUser.id());
    }

    @Transactional
    public void deletePost(long postId) {
        if (postService.isExistedById(postId))
            throw new AppException(ErrorCode.POST_NOT_FOUND);

        boolean isAdmin = Objects.equals(CurrentUser.role(), UserRole.ADMIN.toString());
        if (!isAdmin)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postService.deletePost(postId);
    }

    @Transactional
    public void updateCommentPolicy(long postId, UpdateCommentPolicyRequest request) {
        Post post = postService.getPostById(postId);
        post.setCommentPolicy(request.getPolicy());

        postService.updateCommentPolicy(post);
    }

}
