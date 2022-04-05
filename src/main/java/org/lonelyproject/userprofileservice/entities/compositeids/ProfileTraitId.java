package org.lonelyproject.backend.entities.compositeids;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProfileTraitId implements Serializable {

    @Column(name = "user_profile_id", nullable = false, updatable = false)
    private String userProfileId;

    @Column(name = "trait_id", nullable = false, updatable = false)
    private Integer traitId;

    public ProfileTraitId() {
    }

    public ProfileTraitId(String userProfileId, Integer traitId) {
        this.userProfileId = userProfileId;
        this.traitId = traitId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public Integer getTraitId() {
        return traitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfileTraitId that = (ProfileTraitId) o;
        return userProfileId.equals(that.userProfileId) && traitId.equals(that.traitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, traitId);
    }
}
