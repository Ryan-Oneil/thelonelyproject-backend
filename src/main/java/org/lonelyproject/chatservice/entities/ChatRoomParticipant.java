package org.lonelyproject.chatservice.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.chatservice.entities.compositeids.ParticipantId;

@Entity
public class ChatRoomParticipant {

    @EmbeddedId
    private ParticipantId id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", insertable = false, updatable = false)
    private ChatRoom chatroom;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", insertable = false, updatable = false)
    private UserProfile userProfile;

    public ChatRoomParticipant() {
    }

    public ChatRoomParticipant(ChatRoom chatroom, UserProfile user) {
        this.id = new ParticipantId(user.getId(), chatroom.getId());
        this.chatroom = chatroom;
        this.userProfile = user;
    }

    public ParticipantId getId() {
        return id;
    }

    public void setId(ParticipantId id) {
        this.id = id;
    }

    public ChatRoom getChatroom() {
        return chatroom;
    }

    public void setChatroom(ChatRoom chatroom) {
        this.chatroom = chatroom;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "ChatRoomParticipant{" +
            "id=" + id +
            '}';
    }
}
