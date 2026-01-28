package com.friendhub.dto.websocket;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDTO {
    Long id;
    Long senderId;
    String senderName;
    String senderAvatar;
    Long receiverId;
    String content;
    String messageType;
    Boolean isRead;
    LocalDateTime timestamp;
    Boolean isMe;
}