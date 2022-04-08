package org.lonelyproject.userprofileservice.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTrait;

@Entity
public class Interest extends ProfileTrait {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private InterestCategory category;

    public InterestCategory getCategory() {
        return category;
    }
}
