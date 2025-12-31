package com.friendhub.mapper;

import com.friendhub.dto.response.NotificationResponse;
import com.friendhub.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "receiver", source = "receiver")
    @Mapping(target = "post", source = "post")
    NotificationResponse toNotificationResponse(Notification notification);

}
