package com.friendhub.mapper;

import com.friendhub.dto.request.GroupCreationRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.entity.Group;
import com.friendhub.enums.JoinRequestStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = UserMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    Group toGroup(GroupCreationRequest request);

    @Mapping(target = "totalMembers", source = "totalMembers")
    GroupResponse toResponse(Group group, long totalMembers);

    @Mapping(target = "id", source = "group.id")
    GroupDetailResponse toGroupDetailResponse(Group group,
                                              long totalMembers,
                                              long totalPosts,
                                              boolean isJoined,
                                              JoinRequestStatus joinStatus,
                                              boolean hasPendingRequest);

    void updateGroup(@MappingTarget Group group, GroupUpdateRequest request);

}