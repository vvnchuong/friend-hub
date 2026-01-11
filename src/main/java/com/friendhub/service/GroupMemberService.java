package com.friendhub.service;

import com.friendhub.entity.GroupMember;
import com.friendhub.entity.GroupMemberId;

import java.util.List;

public interface GroupMemberService {

    void save(GroupMember member);

    List<GroupMember> getAllMembersInGroup(long groupId, Long lastId, int limit);

    GroupMember getMemberByIdOrNull(GroupMemberId memberId);

    GroupMember getMemberByIdOrThrow(GroupMemberId memberId);

    void deleteMember(GroupMember member);

    boolean isExistedById(GroupMemberId memberId);

    boolean isGroupMember(long groupId, long userId);

    long countTotalMembers(long groupId);

}
