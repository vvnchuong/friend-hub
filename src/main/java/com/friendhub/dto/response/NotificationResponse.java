package com.friendhub.dto.response;

import com.friendhub.enums.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {

    UserBasicInfoResponse sender;
    UserBasicInfoResponse receiver;
    NotificationType type;
    PostResponse post;
    boolean seen = false;
    Instant createdAt;

}
