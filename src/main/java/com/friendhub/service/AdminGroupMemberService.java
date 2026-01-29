package com.friendhub.service;

import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupMemberResponse;
import com.friendhub.entity.GroupJoinRequest;
import com.friendhub.entity.GroupMember;
import com.friendhub.entity.GroupMemberId;
import com.friendhub.entity.Post;
import com.friendhub.enums.JoinRequestStatus;
import com.friendhub.mapper.GroupMemberMapper;
import com.friendhub.repository.GroupJoinRequestRepository;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminGroupMemberService {

    private final GroupMemberService groupMemberService;
    private final GroupJoinRequestRepository groupJoinRequestRepository;
    private final PostService postService;
    private final GroupMemberMapper groupMemberMapper;

    @Transactional(readOnly = true)
    public CursorResponse<GroupMemberResponse> getAllMembers(
            long groupId, Long lastId) {
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () ->  groupMemberService
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

        performMemberRemoval(groupId, member);
    }

    private void performMemberRemoval(long groupId, GroupMember member) {
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

}