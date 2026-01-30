package com.friendhub.service;

import com.friendhub.dto.request.HandleJoinRequestRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupJoinRequestResponse;
import com.friendhub.dto.response.GroupMemberResponse;
import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.GroupPrivacy;
import com.friendhub.enums.GroupRole;
import com.friendhub.enums.JoinRequestStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.GroupJoinRequestMapper;
import com.friendhub.mapper.GroupMemberMapper;
import com.friendhub.repository.GroupJoinRequestRepository;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupMemberService {

    private final GroupMemberService groupMemberService;
    private final GroupJoinRequestRepository groupJoinRequestRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final PostService postService;
    private final NotificationService notificationService;
    private final GroupMemberMapper groupMemberMapper;
    private final GroupJoinRequestMapper groupJoinRequestMapper;

    @Transactional(readOnly = true)
    public CursorResponse<GroupMemberResponse> getAllMembers(
            long groupId, Long lastId) {
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> groupMemberService
                        .getAllMembersInGroup(groupId, lastId, pageSize + 1),
                gm -> gm.getUser().getId(),
                gm -> gm.stream()
                        .map(groupMemberMapper::toGroupMemberResponse)
                        .toList()
        );
    }

    @Transactional
    public void removeMemberFromGroup(long groupId, long memberId) {
        GroupMemberId id = new GroupMemberId(groupId, memberId);
        GroupMember member = groupMemberService.getMemberByIdOrThrow(id);

        validateAdminOrModerator(groupId, CurrentUser.id());

        if (member.getRole() == GroupRole.ADMIN)
            throw new AppException(ErrorCode.CANNOT_REMOVE_GROUP_ADMIN);

        performMemberRemoval(groupId, member);
    }

    @Transactional
    public void leaveGroup(long groupId) {
        GroupMemberId memberId = new GroupMemberId(groupId, CurrentUser.id());
        GroupMember member = groupMemberService.getMemberByIdOrThrow(memberId);

        performMemberRemoval(groupId, member);
    }

    @Transactional
    public void updateMemberRole(long groupId, long memberId, GroupRole role) {
        GroupMemberId groupMemberId = new GroupMemberId(groupId, CurrentUser.id());
        GroupMember groupMember = groupMemberService.getMemberByIdOrThrow(groupMemberId);

        if (groupMember.getRole() != GroupRole.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (role == GroupRole.ADMIN)
            throw new AppException(ErrorCode.GROUP_ALREADY_HAS_ADMIN);

        GroupMemberId id = new GroupMemberId(groupId, memberId);
        GroupMember member = groupMemberService.getMemberByIdOrThrow(id);

        member.setRole(role);
        groupMemberService.save(member);
    }

    @Transactional
    public void joinGroup(long groupId) {
        Group group = groupService.getGroupById(groupId);

        if (group.getPrivacy() == GroupPrivacy.PRIVATE)
            throw new AppException(ErrorCode.JOIN_NOT_ALLOWED_FOR_PRIVATE_GROUP);

        GroupMemberId memberId = new GroupMemberId(groupId, CurrentUser.id());
        if (groupMemberService.isExistedById(memberId))
            throw new AppException(ErrorCode.USER_ALREADY_MEMBER);

        User user = userService.getUserById(CurrentUser.id());

        GroupJoinRequest request = groupJoinRequestRepository
                .findByUserIdAndGroupId(user.getId(), groupId);

        if (request != null && request.getStatus() != JoinRequestStatus.NONE)
            throw new AppException(ErrorCode.INVALID_JOIN_REQUEST_STATE);

        if (request == null) {
            request = GroupJoinRequest.builder()
                    .group(group)
                    .user(user)
                    .createdAt(Instant.now())
                    .build();
        }

        request.setStatus(JoinRequestStatus.APPROVED);
        groupJoinRequestRepository.save(request);

        createNewGroupMember(group, user);
    }

    @Transactional
    public GroupJoinRequestResponse requestToJoinGroup(long groupId) {
        Group group = groupService.getGroupById(groupId);
        User user = userService.getUserById(CurrentUser.id());

        GroupMemberId memberId = new GroupMemberId(groupId, user.getId());
        if (groupMemberService.isExistedById(memberId))
            throw new AppException(ErrorCode.USER_ALREADY_MEMBER);

        GroupJoinRequest joinRequest =
                groupJoinRequestRepository.findByUserIdAndGroupId(user.getId(), groupId);

        if (joinRequest != null) {
            switch (joinRequest.getStatus()) {
                case PENDING -> throw new AppException(ErrorCode.JOIN_REQUEST_ALREADY_PENDING);
                case APPROVED -> throw new AppException(ErrorCode.USER_ALREADY_MEMBER);
                case NONE, REJECTED -> {
                    joinRequest.setStatus(JoinRequestStatus.PENDING);
                    joinRequest.setCreatedAt(Instant.now());
                    return groupJoinRequestMapper.toGroupJoinRequestResponse(
                            groupJoinRequestRepository.save(joinRequest));
                }
            }
        }

        GroupJoinRequest newRequest = GroupJoinRequest.builder()
                .group(group)
                .user(user)
                .status(JoinRequestStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        return groupJoinRequestMapper.toGroupJoinRequestResponse(
                groupJoinRequestRepository.save(newRequest));
    }

    @Transactional
    public void cancelRequestToJoinGroup(long groupId) {
        User user = userService.getUserById(CurrentUser.id());

        GroupMemberId memberId = new GroupMemberId(groupId, user.getId());
        if (groupMemberService.isExistedById(memberId))
            throw new AppException(ErrorCode.USER_ALREADY_MEMBER);

        GroupJoinRequest request =
                groupJoinRequestRepository
                        .findByUserIdAndGroupId(CurrentUser.id(), groupId);

        if (request == null || request.getStatus() != JoinRequestStatus.PENDING) {
            throw new AppException(ErrorCode.NO_PENDING_JOIN_REQUEST);
        }

        groupJoinRequestRepository.delete(request);
    }

    @Transactional(readOnly = true)
    public List<GroupJoinRequestResponse> getPendingJoinRequests(long groupId) {
        validateAdminOrModerator(groupId, CurrentUser.id());

        return groupJoinRequestRepository
                .findByGroupIdAndStatus(groupId, JoinRequestStatus.PENDING)
                .stream()
                .map(groupJoinRequestMapper::toGroupJoinRequestResponse)
                .toList();
    }

    @Transactional
    public void handleJoinRequest(long groupId, long requestId, HandleJoinRequestRequest request) {
        if (!groupService.isExistedById(groupId))
            throw new AppException(ErrorCode.GROUP_NOT_FOUND);

        GroupJoinRequest joinRequest = groupJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.JOIN_REQUEST_NOT_FOUND));

        if (joinRequest.getGroup().getId() != groupId)
            throw new AppException(ErrorCode.INVALID_JOIN_REQUEST);

        validateAdminOrModerator(joinRequest.getGroup().getId(), CurrentUser.id());

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING)
            throw new AppException(ErrorCode.JOIN_REQUEST_ALREADY_HANDLED);

        User handler = userService.getUserById(CurrentUser.id());

        if (request.isApproved()) {
            joinRequest.setStatus(JoinRequestStatus.APPROVED);

            notificationService.createGroupJoinApprovedNotification(
                    joinRequest.getUser(),
                    joinRequest.getGroup(),
                    handler);

            createNewGroupMember(joinRequest.getGroup(), joinRequest.getUser());
        } else {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
        }

        joinRequest.setHandledBy(handler);
        groupJoinRequestRepository.save(joinRequest);
    }

    private void validateAdminOrModerator(long groupId, long userId) {
        GroupMemberId memberId = new GroupMemberId(groupId, userId);
        GroupMember groupMember = groupMemberService.getMemberByIdOrThrow(memberId);

        if (!(groupMember.getRole() == GroupRole.ADMIN ||
                groupMember.getRole() == GroupRole.MODERATOR))
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    private void performMemberRemoval(long groupId, GroupMember member) {
        if (member.getRole() == GroupRole.ADMIN)
            throw new AppException(ErrorCode.CANNOT_LEAVE_GROUP_AS_ONLY_ADMIN);

        long memberId = member.getUser().getId();

        GroupJoinRequest request = groupJoinRequestRepository
                .findByUserIdAndGroupId(memberId, groupId);
        if (request != null) {
            request.setStatus(JoinRequestStatus.NONE);
            groupJoinRequestRepository.save(request);
        }

        List<Post> posts = postService.getPostByUserAndGroup(memberId, groupId);
        postService.deleteAllPosts(posts);

        groupMemberService.deleteMember(member);
    }

    private void createNewGroupMember(Group group, User user) {
        GroupMemberId memberId = new GroupMemberId(group.getId(), user.getId());

        if (groupMemberService.isExistedById(memberId))
            throw new AppException(ErrorCode.USER_ALREADY_MEMBER);

        GroupMember member = GroupMember.builder()
                .id(memberId)
                .group(group)
                .user(user)
                .role(GroupRole.MEMBER)
                .joinedAt(Instant.now())
                .build();

        groupMemberService.save(member);
    }

}
