package com.friendhub.mapper;

import com.friendhub.dto.request.GroupCreationRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.entity.Group;
import com.friendhub.entity.User;
import com.friendhub.enums.GroupRole;
import com.friendhub.enums.JoinRequestStatus;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring",
        uses = UserMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "members", ignore = true)
    Group toGroup(GroupCreationRequest request);

    @Mapping(target = "id", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    @Mapping(target = "privacy", source = "group.privacy")
    @Mapping(target = "description", source = "group.description")
    @Mapping(target = "coverUrl", source = "group.coverUrl")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "status", source = "group.status")
    @Mapping(target = "createdAt", source = "group.createdAt")
    @Mapping(target = "updatedAt", source = "group.updatedAt")
    @Mapping(target = "totalMembers", source = "totalMembers")
    GroupResponse toResponse(Group group, User creator, long totalMembers);

    @Mapping(target = "id", source = "group.id")
    @Mapping(target = "coverUrl", source = "group.coverUrl")
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "createdAt", source = "group.createdAt")
    @Mapping(target = "updatedAt", source = "group.updatedAt")
    @Mapping(target = "status", source = "creator.status", ignore = true)
    GroupDetailResponse toGroupDetailResponse(Group group,
                                              User creator,
                                              long totalMembers,
                                              long totalPosts,
                                              boolean isJoined,
                                              JoinRequestStatus status,
                                              GroupRole role,
                                              boolean hasPendingRequest);

    void updateGroup(@MappingTarget Group group, GroupUpdateRequest request);


}