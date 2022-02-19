package org.lonelyproject.backend.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String name;

    private String about;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private ProfilePicture picture;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "userProfile")
    private List<ProfileMedia> medias;

    public UserProfile(String id) {
        this.id = id;
    }

    public UserProfile(String name, String about, User user) {
        this.name = name;
        this.about = about;
        this.user = user;
    }

    public UserProfile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ProfilePicture getPicture() {
        return picture;
    }

    public void setPicture(ProfilePicture picture) {
        this.picture = picture;
        this.picture.setUserProfile(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ProfileMedia> getMedias() {
        return medias;
    }

    public void setMedias(List<ProfileMedia> galleryMedia) {
        this.medias = galleryMedia;
    }
}