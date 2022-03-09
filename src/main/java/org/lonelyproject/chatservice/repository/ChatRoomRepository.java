package org.lonelyproject.chatservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.lonelyproject.chatservice.entities.ChatRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, UUID> {

    @Query("select p.chatroom from ChatRoomParticipant p where p.id.userId = ?1")
    List<ChatRoom> getAllChatroomByUserId(String userId);

    @Query("select p.chatroom from ChatRoomParticipant p where p.id.userId = ?1 and p.id.chatroomId = ?2")
    Optional<ChatRoom> getChatroomByIdAndParticipantId(String userId, UUID chatroomId);
}