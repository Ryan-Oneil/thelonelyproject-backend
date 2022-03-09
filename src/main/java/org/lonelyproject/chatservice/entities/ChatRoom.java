package org.lonelyproject.chatservice.entities;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import org.lonelyproject.chatservice.enums.ChatRoomType;

@Entity
public class ChatRoom {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    private String icon;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<ChatRoomParticipant> participants;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @OrderBy("timestamp asc")
    private List<ChatMessage> messages;

    public ChatRoom() {
    }

    public ChatRoom(UUID id, String name, String icon, List<ChatRoomParticipant> participants, ChatRoomType type,
        List<ChatMessage> messages) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.participants = participants;
        this.type = type;
        this.messages = messages;
    }

    public UUID getId() {
        return id;
    }

    public List<ChatRoomParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChatRoomParticipant> participants) {
        this.participants = participants;
    }

    public ChatRoomType getType() {
        return type;
    }

    public void setType(ChatRoomType chatRoomType) {
        this.type = chatRoomType;
    }

    public String getName() {
        return name;
    }

    public void setName(String roomName) {
        this.name = roomName;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        this.getMessages().add(message);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
