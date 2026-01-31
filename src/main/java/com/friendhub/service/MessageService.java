package com.friendhub.service;

import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.websocket.ChatMessageDTO;
import com.friendhub.entity.Message;
import com.friendhub.entity.User;
import com.friendhub.repository.MessageRepository;
import com.friendhub.repository.UserRepository;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageDTO sendMessage(Long senderId, Long receiverId, String content) {
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(Message.MessageType.TEXT)
                .isRead(false)
                .build();

        message = messageRepository.save(message);

        return convertToDTO(message, senderId);
    }

    @Transactional(readOnly = true)
    public CursorResponse<ChatMessageDTO> getChatHistory(
            Long user1Id, Long user2Id, Long currentUserId, Long lastId) {
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> messageRepository
                        .findChatMessages(user1Id, user2Id, lastId, pageSize + 1),
                Message::getId,
                m -> m.stream()
                        .map(msg -> convertToDTO(msg, currentUserId))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(true);
            messageRepository.save(message);
        });
    }

    private ChatMessageDTO convertToDTO(Message message, Long currentUserId) {
        User sender = userRepository.findById(message.getSenderId()).orElse(null);

        return ChatMessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .senderName(sender != null ? sender.getFirstName() + " " + sender.getLastName() : "Unknown")
                .senderAvatar(sender != null ? sender.getAvatarUrl() : null)
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .messageType(message.getMessageType().name())
                .isRead(message.getIsRead())
                .timestamp(message.getCreatedAt())
                .isMe(message.getSenderId().equals(currentUserId))
                .build();
    }
}