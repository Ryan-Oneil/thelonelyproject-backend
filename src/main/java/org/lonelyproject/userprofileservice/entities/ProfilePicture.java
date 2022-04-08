package org.lonelyproject.userprofileservice.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import org.lonelyproject.userprofileservice.entities.supers.CloudItem;

@Entity
public class ProfilePicture extends CloudItem {

    @OneToOne
    private UserProfile userProfile;

    public ProfilePicture() {
    }

    public ProfilePicture(CloudItemDetails itemDetails, String url) {
        super(itemDetails, url);
    }

    public ProfilePicture(CloudItemDetails itemDetails, String url, UserProfile userProfile) {
        super(itemDetails, url);
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
