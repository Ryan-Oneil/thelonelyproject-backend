package org.lonelyproject.chatservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.backend.util.ClassMapperUtil;
import org.lonelyproject.chatservice.dto.ChatMessageDto;
import org.lonelyproject.chatservice.dto.ChatRoomDto;
import org.lonelyproject.chatservice.entities.ChatMessage;
import org.lonelyproject.chatservice.entities.ChatRoom;
import org.lonelyproject.chatservice.entities.ChatRoomParticipant;
import org.lonelyproject.chatservice.enums.ChatRoomType;
import org.lonelyproject.chatservice.repository.ChatMessageRepository;
import org.lonelyproject.chatservice.repository.ChatRoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
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

    public ChatRoomDto getChatMessages(UUID roomId, String participantId) {
        ChatRoom chatRoom = chatRoomRepository.getChatroomByIdAndParticipantId(participantId, roomId)
            .orElseThrow(() -> new ResourceNotFound("Chat doesn't exist"));

        ChatRoomDto chatRoomDto = transformChatroomForUser(chatRoom, participantId);
        chatRoomDto.setMessages(ClassMapperUtil.mapListIgnoreLazyCollection(chatRoom.getMessages(), ChatMessageDto.class));

        return chatRoomDto;
    }

    public ChatRoom getChatRoomById(UUID roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFound("Chat room doesn't exist"));
    }

    public List<ChatRoomDto> getUsersChatroom(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.getAllChatroomByUserId(userId);

        return chatRooms.stream()
            .map(chatRoom -> {
                ChatRoomDto chatRoomDto = transformChatroomForUser(chatRoom, userId);
                ChatMessageDto lastMessage = getChatLastMessage(chatRoom.getId());
                chatRoomDto.setMessages(List.of(lastMessage));

                return chatRoomDto;
            })
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

    public void sendMessageToUser(String userid, String message) {
        messagingTemplate.convertAndSendToUser(userid, "/queue/messages", message);
    }

    @Transactional
    public void registerMessageToChat(ChatMessageDto messageDto, UUID chatId, UserAuth sender) {
        ChatRoom room = getChatRoomById(chatId);
        List<ChatRoomParticipant> participants = room.getParticipants();

        if (!isParticipantOfChat(participants, sender.getId())) {
            throw new AccessDeniedException("You aren't in this chat room");
        }
        ChatMessage chatMessage = new ChatMessage(room, new User(sender.getId()), messageDto.getContent(), new Date());
        room.addMessage(chatMessage);
        chatRoomRepository.save(room);

        notifyChatRoomParticipants(chatMessage, participants);
    }

    public void notifyChatRoomParticipants(ChatMessage chatMessage, List<ChatRoomParticipant> participants) {
        ChatRoomDto chatRoomDto = ClassMapperUtil.mapClassIgnoreLazy(chatMessage.getChatRoom(), ChatRoomDto.class);
        ChatMessageDto messageDto = ClassMapperUtil.mapClassIgnoreLazy(chatMessage, ChatMessageDto.class);
        chatRoomDto.setMessages(List.of(messageDto));

        participants
            .stream()
            .filter(chatRoomParticipant -> !chatRoomParticipant.getId().getUserId().equals(chatMessage.getSender().getId()))
            .forEach(chatRoomParticipant -> messagingTemplate.convertAndSendToUser(chatRoomParticipant.getId().getUserId(), "/queue/messages",
                chatRoomDto));
    }

    public boolean isParticipantOfChat(List<ChatRoomParticipant> participants, String senderId) {
        return participants.stream()
            .anyMatch(chatRoomParticipant -> chatRoomParticipant.getId().getUserId().equals(senderId));
    }

    public ChatMessageDto getChatLastMessage(UUID chatId) {
        Optional<ChatMessage> message = chatMessageRepository.getFirstByChatRoom_IdOrderByTimestamp(chatId);
        ChatMessage lastMessage = message.orElseGet(ChatMessage::new);

        return ClassMapperUtil.mapClassIgnoreLazy(lastMessage, ChatMessageDto.class);
    }
}
