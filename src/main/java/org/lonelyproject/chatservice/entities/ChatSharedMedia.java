package org.lonelyproject.chatservice.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.userprofileservice.entities.CloudItemDetails;
import org.lonelyproject.userprofileservice.entities.supers.MediaCloudItem;

@Entity
public class ChatSharedMedia extends MediaCloudItem {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, unique = true)
    private ChatMessage chatMessage;

    public ChatSharedMedia() {
    }

    public ChatSharedMedia(CloudItemDetails itemDetails) {
        super(itemDetails);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
