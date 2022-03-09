package org.lonelyproject.chatservice.dto;

import java.io.Serializable;
import java.util.Date;

public class ChatMessageDto implements Serializable {

    private Integer id;
    private String senderId;
    private String content;
    private Date timestamp;

    public ChatMessageDto() {
    }

    public ChatMessageDto(Integer id, String senderId, String content, Date timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "sender = " + senderId + ", " +
            "content = " + content + ", " +
            "timestamp = " + timestamp + ")";
    }
}
