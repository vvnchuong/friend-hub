package com.friendhub.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/user");

        config.setApplicationDestinationPrefixes("/app");

        config.setUserDestinationPrefix("/user");

        log.info("Message broker configured");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        log.info("STOMP endpoint registered at /ws");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String id = accessor.getFirstNativeHeader("user-id");

                    if (id != null) {
                        accessor.setUser(() -> id);
                        log.info("User connected: {}", id);
                    } else {
                        log.warn("User connected without user-id header");
                    }
                }

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    String destination = accessor.getDestination();
                    String userId = accessor.getUser() != null ? accessor.getUser().getName() : "unknown";
                    log.info("User {} subscribed to: {}", userId, destination);
                }

                if (StompCommand.SEND.equals(accessor.getCommand())) {
                    String destination = accessor.getDestination();
                    String userId = accessor.getUser() != null ? accessor.getUser().getName() : "unknown";
                    log.info("User {} sending to: {}", userId, destination);
                }

                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    String userId = accessor.getUser() != null ? accessor.getUser().getName() : "unknown";
                    log.info("User disconnected: {}", userId);
                }

                return message;
            }
        });
    }
}