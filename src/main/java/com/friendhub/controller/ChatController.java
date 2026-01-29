package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.websocket.ChatMessageDTO;
import com.friendhub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @GetMapping("/user/{userId}")
    public ApiResponse<CursorResponse<ChatMessageDTO>> getChatHistory(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestParam(required = false) Long lastId) {

        CursorResponse<ChatMessageDTO> messages = messageService
                .getChatHistory(currentUserId, userId, currentUserId, lastId);

        return ApiResponse.<CursorResponse<ChatMessageDTO>>builder()
                .result(messages)
                .build();
    }

    @PostMapping("/{messageId}/read")
    public ApiResponse<String> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ApiResponse.<String>builder()
                .result("Message marked as read")
                .build();
    }
}