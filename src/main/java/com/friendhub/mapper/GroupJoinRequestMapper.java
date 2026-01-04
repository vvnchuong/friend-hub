package com.friendhub.mapper;

import com.friendhub.dto.response.GroupJoinRequestResponse;
import com.friendhub.entity.GroupJoinRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface GroupJoinRequestMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "coverUrl", source = "group.coverUrl")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "handledBy", source = "handledBy")
    GroupJoinRequestResponse toGroupJoinRequestResponse(GroupJoinRequest request);

}
