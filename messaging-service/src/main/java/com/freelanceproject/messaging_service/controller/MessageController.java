package com.freelanceproject.messaging_service.controller;

import com.freelanceproject.messaging_service.model.Chat;
import com.freelanceproject.messaging_service.model.Message;
import com.freelanceproject.messaging_service.service.MessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class MessageController {

    private final MessagingService messagingService;

    public MessageController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/start")
    public ResponseEntity<Chat> startChat(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return ResponseEntity.ok(messagingService.startChat(user1Id, user2Id));
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<Message> sendMessage(@PathVariable Long chatId, @RequestParam Long senderId, @RequestParam String content) {
        return ResponseEntity.ok(messagingService.sendMessage(chatId, senderId, content));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long chatId) {
        return ResponseEntity.ok(messagingService.getMessages(chatId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getUserChats(@PathVariable Long userId) {
        return ResponseEntity.ok(messagingService.getUserChats(userId));
    }
}
