package com.friendhub.controller;

import com.friendhub.dto.websocket.ChatMessageDTO;
import com.friendhub.dto.websocket.TypingDTO;
import com.friendhub.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessageDTO messageDTO) {
        log.info("Received message from {} to {}: {}",
                messageDTO.getSenderId(),
                messageDTO.getReceiverId(),
                messageDTO.getContent());

        ChatMessageDTO savedMessage = messageService.sendMessage(
                messageDTO.getSenderId(),
                messageDTO.getReceiverId(),
                messageDTO.getContent()
        );

        log.info("Message saved with ID: {}", savedMessage.getId());

        String receiver = "/user/" + messageDTO.getReceiverId() + "/queue/messages";
        messagingTemplate.convertAndSendToUser(
                messageDTO.getReceiverId().toString(),
                "/queue/messages",
                savedMessage
        );
        log.info("Sent to receiver: {}", receiver);
        log.info("Message broadcast successfully");
    }

    @MessageMapping("/typing")
    public void typing(@Payload TypingDTO typingDTO) {
        log.info("Typing from {} to {}: {}",
                typingDTO.getSenderId(),
                typingDTO.getReceiverId(),
                typingDTO.getTyping());

        String dest = "/user/" + typingDTO.getReceiverId() + "/queue/typing";
        messagingTemplate.convertAndSendToUser(
                typingDTO.getReceiverId().toString(),
                "/queue/typing",
                typingDTO
        );

        log.info("Typing sent to: {}", dest);
    }
}