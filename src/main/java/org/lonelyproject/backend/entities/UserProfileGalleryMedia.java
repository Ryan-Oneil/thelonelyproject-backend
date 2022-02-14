package org.lonelyproject.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.backend.entities.supers.CloudItem;
import org.lonelyproject.backend.enums.MediaType;

@Entity
public class UserProfileGalleryMedia extends CloudItem {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    public UserProfileGalleryMedia() {
    }

    public UserProfileGalleryMedia(CloudItemDetails itemDetails, String url, MediaType type, UserProfile userProfile) {
        super(itemDetails, url);
        this.type = type;
        this.userProfile = userProfile;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
