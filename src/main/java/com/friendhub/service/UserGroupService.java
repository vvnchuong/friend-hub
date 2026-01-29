package com.friendhub.service;

import com.friendhub.dto.request.GroupCreationRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.GroupRole;
import com.friendhub.enums.GroupStatus;
import com.friendhub.enums.JoinRequestStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.GroupMapper;
import com.friendhub.repository.GroupJoinRequestRepository;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;
    private final PostService postService;
    private final GroupJoinRequestRepository groupJoinRequestRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupResponse createGroup(GroupCreationRequest request) {
        User creator = userService.getUserById(CurrentUser.id());

        Group group = groupMapper.toGroup(request);
        group.setCreatedBy(creator.getId());
        group.setStatus(GroupStatus.ACTIVE);
        group = groupService.createGroup(group);

        GroupMemberId memberId = new GroupMemberId(group.getId(), CurrentUser.id());
        GroupMember adminMember = GroupMember.builder()
                .id(memberId)
                .group(group)
                .user(creator)
                .role(GroupRole.ADMIN)
                .build();

        groupMemberService.save(adminMember);

        return groupMapper.toResponse(group, creator, 1);
    }

    @Transactional(readOnly = true)
    public CursorResponse<GroupResponse> getSuggestedGroups(
            String keyword, Long lastId) {
        int pageSize = 9;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> groupService
                        .getSuggestedGroups(
                                CurrentUser.id(), keyword, lastId, pageSize + 1),
                Group::getId,
                g -> g.stream()
                        .map(this::mapToGroupResponse)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public GroupDetailResponse getGroupDetail(long groupId) {
        Group group = groupService.getGroupById(groupId);
        User user = userService.getUserById(CurrentUser.id());

        long countTotalMembers = groupMemberService.countTotalMembers(groupId);
        long countTotalPosts = postService.countTotalPostsByGroupId(groupId);

        GroupMemberId memberId = new GroupMemberId(groupId, CurrentUser.id());
        GroupMember currentMember = groupMemberService.getMemberByIdOrNull(memberId);

        GroupJoinRequest request = groupJoinRequestRepository
                 .findByUserIdAndGroupId(CurrentUser.id(), group.getId());

        JoinRequestStatus joinRequestStatus = null;
        if (request != null)
            joinRequestStatus = request.getStatus();

        boolean isJoined = joinRequestStatus == JoinRequestStatus.APPROVED;

        boolean hasPendingRequest = false;

        if (currentMember == null)
            hasPendingRequest = groupJoinRequestRepository
                    .existsByGroupIdAndUserIdAndStatus(
                            groupId, CurrentUser.id(), JoinRequestStatus.PENDING);

        if (group.getStatus() == GroupStatus.BANNED)
            throw new AppException(ErrorCode.GROUP_BANNED);

       return groupMapper.toGroupDetailResponse(group,
               user,
               countTotalMembers,
               countTotalPosts,
               isJoined,
               joinRequestStatus,
               currentMember != null ? currentMember.getRole() : null,
               hasPendingRequest);
    }

    @Transactional(readOnly = true)
    public CursorResponse<GroupResponse> getMyGroups(String keyword, Long lastId) {
        int pageSize = 9;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> groupService
                        .getMyGroups(CurrentUser.id(), keyword, lastId, pageSize + 1),
                Group::getId,
                g -> g.stream()
                        .map(this::mapToGroupResponse)
                        .toList()
        );
    }

    @Transactional
    public GroupResponse updateGroup(long groupId, GroupUpdateRequest request) {
        Group group = groupService.getGroupById(groupId);

        if (!(group.getCreatedBy() == CurrentUser.id()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (request.getName() != null && request.getName().isBlank())
            throw new AppException(ErrorCode.GROUP_NAME_INVALID);

        groupMapper.updateGroup(group, request);
        group.setUpdatedAt(Instant.now());
        group = groupService.updateGroup(group);

        User creator = userService.getUserById(CurrentUser.id());

        long totalMembers = groupMemberService.countTotalMembers(groupId);

        return groupMapper.toResponse(group, creator, totalMembers);
    }

    @Transactional
    public void deleteGroup(long groupId) {
        Group group = groupService.getGroupById(groupId);

        if (!(group.getCreatedBy() == CurrentUser.id()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        groupService.deleteGroup(groupId);
    }

    private GroupResponse mapToGroupResponse(Group group) {
        User creator = userService.getUserById(group.getCreatedBy());

        long totalMembers = groupMemberService.countTotalMembers(group.getId());

        return groupMapper.toResponse(group, creator, totalMembers);
    }

}
