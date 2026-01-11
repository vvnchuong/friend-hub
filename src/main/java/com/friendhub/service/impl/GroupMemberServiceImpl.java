package com.friendhub.service.impl;

import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.*;
import com.friendhub.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Override
    public void save(GroupMember member) {
        groupMemberRepository.save(member);
    }

    @Override
    public List<GroupMember> getAllMembersInGroup(
            long groupId, Long lastId, int limit) {
        return groupMemberRepository
                .findAllMembersInGroup(groupId, lastId, limit);
    }

    @Override
    public GroupMember getMemberByIdOrNull(GroupMemberId memberId) {
        return groupMemberRepository.findById(memberId)
                .orElse(null);
    }

    @Override
    public GroupMember getMemberByIdOrThrow(GroupMemberId memberId) {
        return groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
    }

    @Override
    public void deleteMember(GroupMember member) {
        groupMemberRepository.delete(member);
    }

    @Override
    public boolean isExistedById(GroupMemberId memberId) {
        return groupMemberRepository.existsById(memberId);
    }

    @Override
    public boolean isGroupMember(long groupId, long userId) {
        GroupMemberId id = new GroupMemberId(groupId, userId);

        return groupMemberRepository.existsById(id);
    }

    @Override
    public long countTotalMembers(long groupId) {
        return groupMemberRepository.countByGroupId(groupId);
    }


}
