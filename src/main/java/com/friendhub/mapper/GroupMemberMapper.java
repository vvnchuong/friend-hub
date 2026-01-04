package com.friendhub.mapper;

import com.friendhub.dto.response.GroupMemberResponse;
import com.friendhub.entity.GroupMember;
import com.friendhub.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface GroupMemberMapper {

    @Mapping(target = "role", source = "groupMember.role")
    GroupMemberResponse toGroupMemberResponse(GroupMember groupMember, User user);

    GroupMemberResponse toGroupMemberResponse(GroupMember groupMember);

}
