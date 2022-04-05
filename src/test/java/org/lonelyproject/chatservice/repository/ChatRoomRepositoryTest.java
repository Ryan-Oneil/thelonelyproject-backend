package org.lonelyproject.chatservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.lonelyproject.userprofileservice.repository.BaseRepository;
import org.lonelyproject.chatservice.entities.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRoomRepositoryTest extends BaseRepository {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void getAllChatroomByUserIdTest() {
        List<ChatRoom> chatRoomList = chatRoomRepository.getAllChatroomByUserId("1");

        assertThat(chatRoomList).isNotEmpty();
        assertThat(chatRoomList.size()).isEqualTo(1);
    }

    @Test
    void getChatroomByIdAndParticipantIdTest() {
        Optional<ChatRoom> chatRoom = chatRoomRepository.getChatroomByIdAndParticipantId("3",
            UUID.fromString("95c97c8a-7fe3-4aed-9ceb-26acad52575e"));

        assertThat(chatRoom).isNotPresent();
    }

    @Test
    void getChatRoomByParticipantsTest() {
        Optional<ChatRoom> chatRoom = chatRoomRepository.getChatRoomByParticipants("1", "2");

        assertThat(chatRoom).isPresent();
    }
}
