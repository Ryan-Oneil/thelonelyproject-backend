package org.lonelyproject.backend.entities.compositeids;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MatchId implements Serializable {

    @Column(name = "profile_id", nullable = false, updatable = false)
    private String profileId;

    @Column(name = "match_profile_id", nullable = false, updatable = false)
    private String matchProfileId;

    public MatchId() {
    }

    public MatchId(String profileId, String matchProfileId) {
        this.profileId = profileId;
        this.matchProfileId = matchProfileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getMatchProfileId() {
        return matchProfileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchId matchId)) {
            return false;
        }
        return getProfileId().equals(matchId.getProfileId()) && getMatchProfileId().equals(matchId.getMatchProfileId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfileId(), getMatchProfileId());
    }
}
