package org.lonelyproject.userprofileservice.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.userprofileservice.entities.compositeids.ProfileTraitId;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTraitRelation;

@Entity
public class UserInterest extends ProfileTraitRelation {

    @ManyToOne(optional = false)
    @JoinColumn(name = "trait_id", insertable = false, updatable = false)
    private Interest interest;

    public UserInterest() {
    }

    public UserInterest(UserProfile userProfile, Interest interest) {
        super(new ProfileTraitId(userProfile.getId(), interest.getId()), userProfile);

        this.interest = interest;
    }

    public Interest getInterest() {
        return interest;
    }

}
