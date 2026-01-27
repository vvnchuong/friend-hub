package com.friendhub.service;

import com.friendhub.dto.request.PostSearchRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserRole;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.repository.specification.PostSpecification;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostService postService;
    private final PostQueryService postQueryService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getAllPosts(
            PostSearchRequest request,
            Pageable pageable) {
        Specification<Post> spec = PostSpecification.build(request);

        Pageable sortedPageable = PageRequest
                .of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt")
                .descending());

        Page<Post> page = postService.getAllPosts(spec, sortedPageable);

        List<PostResponse> responses = page.stream()
                .map(post -> postQueryService.build(post, CurrentUser.id()))
                .toList();

        return PageResponse.<PostResponse>builder()
                .data(responses)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

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
