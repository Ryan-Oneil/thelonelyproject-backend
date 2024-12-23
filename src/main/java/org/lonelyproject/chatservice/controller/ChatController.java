package org.lonelyproject.chatservice.controller;

import org.lonelyproject.auth.dto.UserAuth;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.dto.Formdata;
import org.lonelyproject.chatservice.service.ChatService;
import org.lonelyproject.userprofileservice.service.FileService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    private final FileService fileService;

    public ChatController(ChatService chatService, FileService fileService) {
        this.chatService = chatService;
        this.fileService = fileService;
    }

    @MessageMapping("/chat/{chatID}")
    public void receivedChatMessage(@DestinationVariable UUID chatID, ChatMessageDto chatMessageDto, @AuthenticationPrincipal UserAuth auth) {
        chatService.registerMessageToChat(chatMessageDto, chatID, auth);
    }

    @GetMapping("/messages/{chatID}")
    public ChatRoomDto findChatMessages(@PathVariable UUID chatID, @AuthenticationPrincipal UserAuth auth) {
        return chatService.getChatMessages(chatID, auth.getId());
    }

    @PostMapping("/{chatID}/message/media")
    public ChatMessageDto uploadChatMedia(@PathVariable UUID chatID, @AuthenticationPrincipal UserAuth auth, HttpServletRequest request)
        throws IOException {
        Formdata formdata = fileService.handleFileUpload(request, -1);

        return chatService.registerMediaMessageToChat(formdata, chatID, auth);
    }

    @GetMapping("/chatrooms")
    public List<ChatRoomDto> getUsersChatroom(@AuthenticationPrincipal UserAuth auth) {
        return chatService.getUsersChatroom(auth.getId());
    }

    @PostMapping("/create/{targetId}")
    public ChatRoomDto createChatRoom(@PathVariable String targetId, @AuthenticationPrincipal UserAuth auth) {
        return chatService.getOrCreateChat(auth.getId(), targetId);
    }
}
