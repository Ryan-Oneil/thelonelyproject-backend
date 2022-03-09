package org.lonelyproject.chatservice.repository;

import java.util.Optional;
import java.util.UUID;
import org.lonelyproject.chatservice.entities.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {

    Optional<ChatMessage> getFirstByChatRoom_IdOrderByTimestamp(UUID roomId);
}