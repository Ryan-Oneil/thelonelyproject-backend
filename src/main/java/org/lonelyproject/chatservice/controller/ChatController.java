package org.lonelyproject.chatservice.controller;

import java.util.List;
import java.util.UUID;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/messages/{chatID}")
    public List<ChatMessageDto> findChatMessages(@PathVariable UUID chatID, @AuthenticationPrincipal UserAuth auth) {
        return chatService.getChatMessages(chatID, auth.getId());
    }

    @GetMapping("/chatrooms")
    public List<ChatRoomDto> getUsersChatroom(@AuthenticationPrincipal UserAuth auth) {
        return chatService.getUsersChatroom(auth.getId());
    }
}
