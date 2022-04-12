package org.lonelyproject.userprofileservice.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.userprofileservice.entities.compositeids.ProfileTraitId;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTraitRelation;

@Entity
public class UserSpotifyArtist extends ProfileTraitRelation {

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn
    private Artist artist;

    public UserSpotifyArtist() {
    }

    public UserSpotifyArtist(UserProfile userProfile, Artist artist) {
        super(new ProfileTraitId(userProfile.getId(), artist.getId()), userProfile);
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
