package org.lonelyproject.chatservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.lonelyproject.userprofileservice.dto.UploadedFile;
import org.lonelyproject.userprofileservice.security.UserAuth;
import org.lonelyproject.userprofileservice.service.FileService;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        UploadedFile file = fileService.handleFileUpload(request, -1).get(0);

        return chatService.registerMediaMessageToChat(file, chatID, auth);
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
