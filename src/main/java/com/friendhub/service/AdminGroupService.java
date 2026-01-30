package com.friendhub.service;

import com.friendhub.dto.request.GroupSearchRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.request.GroupUpdateStatusRequest;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.JoinRequestStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.GroupMapper;
import com.friendhub.repository.GroupJoinRequestRepository;
import com.friendhub.repository.specification.GroupSpecification;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdminGroupService {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;
    private final PostService postService;
    private final GroupJoinRequestRepository groupJoinRequestRepository;
    private final GroupMapper groupMapper;

    @Transactional(readOnly = true)
    public PageResponse<GroupResponse> getAllGroups(
            GroupSearchRequest request,
            Pageable pageable) {
        Specification<Group> spec = GroupSpecification.build(request);

        Pageable sortedPageable = PageRequest
                .of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("createdAt")
                                .descending());

        Page<Group> page = groupService.getAllGroups(spec, sortedPageable);

        return PageResponse.<GroupResponse>builder()
                .data(page.getContent().stream()
                        .map(group -> {
                            long countTotalMembers = groupMemberService.countTotalMembers(group.getId());
                    return groupMapper.toResponse(group, countTotalMembers);
                }).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public GroupDetailResponse getGroupDetail(long groupId) {
        Group group = groupService.getGroupById(groupId);

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

        return groupMapper.toGroupDetailResponse(group,
                countTotalMembers,
                countTotalPosts,
                isJoined,
                joinRequestStatus,
                hasPendingRequest);
    }

    @Transactional
    public GroupResponse updateGroup(long groupId, GroupUpdateRequest request) {
        Group group = groupService.getGroupById(groupId);

        if (request.getName() != null && request.getName().isBlank())
            throw new AppException(ErrorCode.GROUP_NAME_INVALID);

        groupMapper.updateGroup(group, request);
        group.setUpdatedAt(Instant.now());
        group = groupService.updateGroup(group);

        long totalMembers = groupMemberService.countTotalMembers(groupId);

        return groupMapper.toResponse(group, totalMembers);
    }

    @Transactional
    public void deleteGroup(long groupId) {
        if (!groupService.isExistedById(groupId))
            throw new AppException(ErrorCode.GROUP_NOT_FOUND);

        groupService.deleteGroup(groupId);
    }

    @Transactional
    public void updateGroupStatus(
            long groupId, GroupUpdateStatusRequest request) {
        groupService.updateGroupStatus(groupId, request.getStatus().name());
    }

}
