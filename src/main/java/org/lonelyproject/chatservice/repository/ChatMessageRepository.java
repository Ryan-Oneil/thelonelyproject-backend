package org.lonelyproject.chatservice.repository;

import org.lonelyproject.chatservice.entities.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {

}