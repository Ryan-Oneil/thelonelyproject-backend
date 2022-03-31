package org.lonelyproject.chatservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.service.BaseServiceTest;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.springframework.beans.factory.annotation.Autowired;

class ChatServiceTest extends BaseServiceTest {

    @Autowired
    private ChatService chatService;

    @Test
    @Transactional
    void getChatMessagesValidTest() {
        ChatRoomDto chatRoomDto = chatService.getChatMessages(UUID.fromString("95c97c8a-7fe3-4aed-9ceb-26acad52575e"), "1");

        assertThat(chatRoomDto).isNotNull();
    }

    @Test
    void getChatMessagesInValidTest() {
        ResourceNotFound exception = Assertions.assertThrows(ResourceNotFound.class,
            () -> chatService.getChatMessages(UUID.fromString("95c97c8a-7fe3-4aed-9ceb-26acad52575e"), "3"));
        assertThat(exception.getMessage()).isEqualTo("Chat doesn't exist");
    }
}
