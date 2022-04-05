package org.lonelyproject.userprofileservice.entities.supers;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.lonelyproject.userprofileservice.entities.UserProfile;
import org.lonelyproject.userprofileservice.entities.compositeids.ProfileTraitId;

@MappedSuperclass
public class ProfileTraitRelation {

    @EmbeddedId
    private ProfileTraitId profileTraitId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_profile_id", insertable = false, updatable = false)
    private UserProfile userProfile;

    public ProfileTraitRelation() {
    }

    public ProfileTraitRelation(ProfileTraitId profileTraitId, UserProfile userProfile) {
        this.profileTraitId = profileTraitId;
        this.userProfile = userProfile;
    }

    public ProfileTraitId getProfileTraitId() {
        return profileTraitId;
    }

    public void setProfileTraitId(ProfileTraitId profileTraitId) {
        this.profileTraitId = profileTraitId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
