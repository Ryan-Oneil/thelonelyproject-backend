package org.lonelyproject.chatservice.entities.compositeids;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParticipantId implements Serializable {

    @Column(name = "user_profile_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "chatroom_id", nullable = false, updatable = false)
    private UUID chatroomId;

    public ParticipantId() {
    }

    public ParticipantId(String userId, UUID chatroomId) {
        this.userId = userId;
        this.chatroomId = chatroomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(UUID chatroomId) {
        this.chatroomId = chatroomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantId that)) {
            return false;
        }
        return getUserId().equals(that.getUserId()) && getChatroomId().equals(that.getChatroomId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getChatroomId());
    }

    @Override
    public String toString() {
        return "ParticipantId{" +
            "userId='" + userId + '\'' +
            ", chatroomId=" + chatroomId +
            '}';
    }
}
