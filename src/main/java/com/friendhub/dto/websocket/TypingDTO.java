package com.friendhub.dto.websocket;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingDTO {

    Long senderId;
    Long receiverId;
    Boolean typing;

}
