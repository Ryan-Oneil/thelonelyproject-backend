package org.lonelyproject.chatservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.backend.service.BaseServiceTest;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.entities.ChatRoom;
import org.lonelyproject.chatservice.enums.ChatRoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

@Transactional
class ChatServiceTest extends BaseServiceTest {

    public static final UUID CHATID = UUID.fromString("95c97c8a-7fe3-4aed-9ceb-26acad52575e");

    @Autowired
    private ChatService chatService;

    @Test
    void getChatMessagesValidTest() {
        ChatRoomDto chatRoomDto = chatService.getChatMessages(CHATID, "1");

        assertThat(chatRoomDto).isNotNull();
    }

    @Test
    void getChatMessagesInValidTest() {
        ResourceNotFound exception = Assertions.assertThrows(ResourceNotFound.class,
            () -> chatService.getChatMessages(CHATID, "3"));
        assertThat(exception.getMessage()).isEqualTo("Chat doesn't exist");
    }

    @Test
    void getChatRoomByIdValidTest() {
        ChatRoom chatRoom = chatService.getChatRoomById(CHATID);

        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(CHATID);
    }

    @Test
    void getChatRoomByIdInValidTest() {
        ResourceNotFound exception = Assertions.assertThrows(ResourceNotFound.class,
            () -> chatService.getChatRoomById(UUID.fromString("74cf5bf4-09bf-48db-abb7-1d3616e374f8")));
        assertThat(exception.getMessage()).isEqualTo("Chat doesn't exist");
    }

    @Test
    void getUsersChatroomTest() {
        List<ChatRoomDto> chatRooms = chatService.getUsersChatroom("1");

        assertThat(chatRooms).isNotEmpty();
    }

    @Test
    void getUsersChatroomNoneFoundTest() {
        List<ChatRoomDto> chatRooms = chatService.getUsersChatroom("5");

        assertThat(chatRooms).isEmpty();
    }

    @Test
    void transformChatroomForUserTest() {
        ChatRoom chatRoom = chatService.getChatRoomById(CHATID);

        ChatRoomDto chatRoomDto = chatService.transformChatroomForUser(chatRoom, "1");

        assertThat(chatRoomDto).isNotNull();
        assertThat(chatRoomDto.getType()).isEqualTo(ChatRoomType.DIRECT);
        assertThat(chatRoomDto.getAbout()).isNotBlank();
        assertThat(chatRoomDto.getIcon()).isNotBlank();
    }

    @Test
    void registerMessageToChatTest() {
        ChatMessageDto chatMessageDto = new ChatMessageDto(1, "1", "test", new Date());

        chatService.registerMessageToChat(chatMessageDto, CHATID, new UserAuth("1", "test", true, ""));
        ChatRoomDto chatRoomDto = chatService.getChatMessages(CHATID, "1");

        assertThat(chatRoomDto.getMessages().size()).isGreaterThan(2);
    }

    @Test
    void registerMessageToChatInvalidTest() {
        ChatMessageDto chatMessageDto = new ChatMessageDto(1, "4", "test", new Date());

        AccessDeniedException exception = Assertions.assertThrows(AccessDeniedException.class,
            () -> chatService.registerMessageToChat(chatMessageDto, CHATID, new UserAuth("4", "test", true, "")));
        assertThat(exception.getMessage()).isEqualTo("You aren't in this chat room");
    }

    @Test
    void getChatLastMessageTest() {
        ChatMessageDto chatMessageDto = chatService.getChatLastMessage(CHATID);

        assertThat(chatMessageDto).isNotNull();
    }

    @Test
    void getOrCreateChatTest() {
        ChatRoomDto chatRoomDto = chatService.getOrCreateChat("1", "2");

        assertThat(chatRoomDto.getId()).isEqualTo(CHATID);
    }

    @Test
    void getOrCreateChatNewTest() {
        ChatRoomDto chatRoomDto = chatService.getOrCreateChat("1", "3");

        assertThat(chatRoomDto.getId()).isNotEqualTo(CHATID);
    }

    @Test
    void getOrCreateChatErrorTest() {
        AccessDeniedException exception = Assertions.assertThrows(AccessDeniedException.class,
            () -> chatService.getOrCreateChat("1", "4"));
        assertThat(exception.getMessage()).isEqualTo("You must be connected with this user to start a chat");
    }
}
