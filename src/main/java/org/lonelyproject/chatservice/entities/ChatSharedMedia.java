package org.lonelyproject.chatservice.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.lonelyproject.userprofileservice.entities.CloudItemDetails;
import org.lonelyproject.userprofileservice.entities.supers.CloudItem;

@Entity
public class ChatSharedMedia extends CloudItem {

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private ChatMessage chatMessage;

    public ChatSharedMedia() {
    }

    public ChatSharedMedia(CloudItemDetails itemDetails, String url) {
        super(itemDetails, url);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
