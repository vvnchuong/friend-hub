package com.friendhub.service;

import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminGroupPostService {

    private final GroupPostService groupPostService;
    private final GroupService groupService;
    private final PostService postService;
    private final PostQueryService postQueryService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public CursorResponse<PostResponse> getAllPosts(long groupId, Long lastId) {
        int pageSize = 2;

        List<Post> posts = groupPostService
                .getAllPosts(groupId, lastId, pageSize + 1);

        boolean hasNext = posts.size() > pageSize;
        if (hasNext)
            posts = posts.subList(0, pageSize);

        Long nextCursor = hasNext ? posts.getLast().getId() : null;

        List<PostResponse> responses = posts.stream()
                .map(post -> postQueryService.build(post, CurrentUser.id()))
                .toList();

        return CursorResponse.<PostResponse>builder()
                .data(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(long groupId, long postId) {
        Post post = groupPostService.getPostById(postId, groupId);

        return postQueryService.build(post, CurrentUser.id());
    }

    @Transactional
    public PostResponse updatePost(
            long groupId, long postId, PostUpdateRequest request) {
        Post post = groupPostService.getPostById(groupId ,postId);

        postMapper.updatePost(post, request);

        groupPostService.updatePost(post);

        return postMapper.toPostResponse(post);
    }

    @Transactional
    public void deletePost(long groupId, long postId) {
        if (groupService.isExistedById(groupId))
            throw new AppException(ErrorCode.GROUP_NOT_FOUND);

        if (postService.isExistedById(postId))
            throw new AppException(ErrorCode.POST_NOT_FOUND);

        groupPostService.deletePost(postId);
    }

}
