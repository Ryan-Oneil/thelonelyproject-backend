package org.lonelyproject.userprofileservice.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import org.lonelyproject.userprofileservice.entities.supers.MediaCloudItem;

@Entity
public class ProfilePicture extends MediaCloudItem {

    @OneToOne
    private UserProfile userProfile;

    public ProfilePicture() {
    }

    public ProfilePicture(CloudItemDetails itemDetails) {
        super(itemDetails);
    }

    public ProfilePicture(CloudItemDetails itemDetails, UserProfile userProfile) {
        super(itemDetails);
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
