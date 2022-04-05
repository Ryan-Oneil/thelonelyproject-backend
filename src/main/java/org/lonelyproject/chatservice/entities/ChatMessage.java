package org.lonelyproject.chatservice.entities;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.lonelyproject.userprofileservice.entities.User;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false, updatable = false)
    private ChatRoom chatRoom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Date timestamp;

    @OneToOne(mappedBy = "chatMessage", cascade = CascadeType.ALL)
    private ChatSharedMedia sharedMedia;

    public ChatMessage() {
    }

    public ChatMessage(ChatRoom chatRoom, User sender, String content, Date timestamp) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatMessage(ChatRoom chatRoom, User sender, String content, Date timestamp, ChatSharedMedia sharedMedia) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.sharedMedia = sharedMedia;
        this.sharedMedia.setChatMessage(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ChatSharedMedia getSharedMedia() {
        return sharedMedia;
    }

    public void setSharedMedia(ChatSharedMedia sharedMedia) {
        this.sharedMedia = sharedMedia;
    }
}
