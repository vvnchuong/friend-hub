package com.friendhub.service;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.GroupPrivacy;
import com.friendhub.enums.GroupRole;
import com.friendhub.enums.Privacy;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupPostService {

    private final GroupPostService groupPostService;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;
    private final PostService postService;
    private final PostQueryService postQueryService;
    private final PostMapper postMapper;
    private final PostMediaMapper postMediaMapper;

    @Transactional
    public PostResponse createPost(long groupId, PostCreationRequest request) {
        Group group = groupService.getGroupById(groupId);

        User user = userService.getUserById(CurrentUser.id());

        if (!groupMemberService.isGroupMember(group.getId(), user.getId()))
            throw new AppException(ErrorCode.GROUP_MEMBER_NOT_FOUND);

        Post post = postMapper.toPost(request);
        post.setGroup(group);
        post.setPrivacy(Privacy.PUBLIC);
        post.setUser(user);

        List<PostMedia> mediaList = request.getMediaList()
                .stream()
                .map(postMediaMapper::toPostMedia)
                .toList();

        Post saved = groupPostService.createPost(post, mediaList);

        return postMapper.toPostResponse(saved);
    }

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
        Group group = groupService.getGroupById(groupId);
        Post post = groupPostService.getPostById(groupId, postId);


        if (group.getPrivacy() != GroupPrivacy.PUBLIC) {
            if (!groupMemberService.isGroupMember(groupId, CurrentUser.id()))
                throw new AppException(ErrorCode.UNAUTHORIZED);
        }


        return postQueryService.build(post, CurrentUser.id());
    }

    @Transactional
    public PostResponse updatePost(
            long groupId, long postId, PostUpdateRequest request) {
        Post post = validatePostEditable(groupId, postId);

        postMapper.updatePost(post, request);

        groupPostService.updatePost(post);

        return postMapper.toPostResponse(post);
    }

    @Transactional
    public void deletePost(long groupId, long postId) {
        Post post = validatePostEditable(groupId, postId);

        groupPostService.deletePost(post.getId());
    }

    private boolean isGroupAdminOrModerator(long groupId) {
        GroupMemberId id = new GroupMemberId(groupId, CurrentUser.id());
        GroupMember member = groupMemberService.getMemberByIdOrThrow(id);

        return member.getRole() == GroupRole.ADMIN ||
                member.getRole() == GroupRole.MODERATOR;
    }

    private Post validatePostEditable(long groupId, long postId) {
        Post post = postService.getPostById(postId);

        if (post.getGroup() == null || post.getGroup().getId() != groupId)
            throw new AppException(ErrorCode.POST_NOT_IN_GROUP);

        boolean isAuthor = post.getUser().getId() == CurrentUser.id();
        boolean isGroupAdmin = isGroupAdminOrModerator(groupId);

        if (!isAuthor && !isGroupAdmin)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return post;
    }

}
