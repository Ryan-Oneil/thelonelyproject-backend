package org.lonelyproject.chatservice.service;

import java.util.List;
import java.util.UUID;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.util.ClassMapperUtil;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.entities.ChatRoom;
import org.lonelyproject.chatservice.enums.ChatRoomType;
import org.lonelyproject.chatservice.repository.ChatMessageRepository;
import org.lonelyproject.chatservice.repository.ChatRoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<ChatMessageDto> getChatMessages(UUID roomId, String participantId) {
        ChatRoom chatRoom = chatRoomRepository.getChatroomByIdAndParticipantId(participantId, roomId)
            .orElseThrow(() -> new ResourceNotFound("Chat doesn't exist"));

        return ClassMapperUtil.mapListIgnoreLazyCollection(chatRoom.getMessages(), ChatMessageDto.class);
    }

    public ChatRoom getChatRoomById(UUID roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFound("Chat room doesn't exist"));
    }

    public List<ChatRoomDto> getUsersChatroom(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.getAllChatroomByUserId(userId);

        return chatRooms.stream()
            .map(chatRoom -> transformChatroomForUser(chatRoom, userId))
            .toList();
    }

    public ChatRoomDto transformChatroomForUser(ChatRoom chatRoom, String userId) {
        ChatRoomDto chatRoomDto = ClassMapperUtil.mapClassIgnoreLazy(chatRoom, ChatRoomDto.class);
        List<UserProfileDto> participants = chatRoom.getParticipants().stream()
            .filter(chatRoomParticipant -> !(chatRoomDto.getType() == ChatRoomType.DIRECT && chatRoomParticipant.getId().getUserId().equals(userId)))
            .map(chatRoomParticipant -> ClassMapperUtil.mapClassIgnoreLazy(chatRoomParticipant.getUserProfile(), UserProfileDto.class))
            .toList();

        chatRoomDto.setParticipants(participants);

        //Sets the chat room name and icon to the other person in the direct message
        if (chatRoomDto.getType() == ChatRoomType.DIRECT) {
            UserProfileDto receiver = participants.get(0);

            chatRoomDto.setIcon(receiver.getProfilePictureUrl());
            chatRoomDto.setName(receiver.getName());
        }
        return chatRoomDto;
    }
}
