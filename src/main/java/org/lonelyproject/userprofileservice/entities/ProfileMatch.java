package org.lonelyproject.backend.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.backend.entities.compositeids.MatchId;

@Entity
public class ProfileMatch {

    @EmbeddedId
    private MatchId matchId;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Date generated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private UserProfile userProfile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match_profile_id", insertable = false, updatable = false)
    private UserProfile matchUserProfile;

    public ProfileMatch() {
    }

    public ProfileMatch(Double score, UserProfile userProfile, UserProfile matchUserProfile) {
        this.matchId = new MatchId(userProfile.getId(), matchUserProfile.getId());
        this.score = score;
        this.generated = new Date();
        this.userProfile = userProfile;
        this.matchUserProfile = matchUserProfile;
    }

    public MatchId getMatchId() {
        return matchId;
    }

    public void setMatchId(MatchId matchId) {
        this.matchId = matchId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getGenerated() {
        return generated;
    }

    public void setGenerated(Date generated) {
        this.generated = generated;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getMatchUserProfile() {
        return matchUserProfile;
    }

    public void setMatchUserProfile(UserProfile matchUserProfile) {
        this.matchUserProfile = matchUserProfile;
    }

    @Override
    public String toString() {
        return "ProfileMatch{" +
            "matchId=" + matchId +
            ", score=" + score +
            ", generated=" + generated +
            ", userProfile=" + userProfile +
            ", matchUserProfile=" + matchUserProfile +
            '}';
    }
}
