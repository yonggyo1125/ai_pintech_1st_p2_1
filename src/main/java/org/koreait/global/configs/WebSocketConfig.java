package org.koreait.global.configs;

import org.koreait.message.websockets.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MessageHandler messageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ws://localhost:3000/message
        String profile = System.getenv("spring.profiles.active");


        registry.addHandler(messageHandler, "msg")
                .setAllowedOrigins(profile.contains("prod") ? "" : "http://localhost:3000");


    }
}
