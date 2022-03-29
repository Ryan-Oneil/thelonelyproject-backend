package org.lonelyproject.chatservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.chatservice.enums.ChatRoomType;

public class ChatRoomDto implements Serializable {

    private UUID id;
    private String name;
    private ChatRoomType type;
    private String icon;
    private String about;
    private List<UserProfileDto> participants;
    private List<ChatMessageDto> messages = new ArrayList<>();

    public ChatRoomDto() {
    }

    public ChatRoomDto(UUID id, String name, ChatRoomType type, String icon, List<UserProfileDto> participants, List<ChatMessageDto> messages) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.participants = participants;
        this.messages = messages;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatRoomType getType() {
        return type;
    }

    public void setType(ChatRoomType type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<UserProfileDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserProfileDto> participants) {
        this.participants = participants;
    }

    public List<ChatMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDto> messages) {
        this.messages = messages;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "ChatRoomDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", icon='" + icon + '\'' +
            ", about='" + about + '\'' +
            ", participants=" + participants +
            ", messages=" + messages +
            '}';
    }
}
