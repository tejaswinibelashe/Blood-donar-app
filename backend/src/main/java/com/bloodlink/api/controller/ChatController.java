package com.bloodlink.api.controller;

import com.bloodlink.api.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload Message message) {
        // Save to Database logic would go here
        // messageRepository.save(message);
        
        // Route the message to the specific receiver's private queue
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getId(),
                "/queue/messages", 
                message
        );
    }
}
