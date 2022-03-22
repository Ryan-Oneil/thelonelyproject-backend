package org.lonelyproject.backend.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "userProfile")
    private List<ProfileMedia> medias;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "userProfile")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserInterest> interests;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "userProfile")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserPrompt> prompts;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ProfileConnection> connections;

    public UserProfile(String id) {
        this.id = id;
    }

    public UserProfile(String name, String about, User user) {
        this.name = name;
        this.about = about;
        this.user = user;
    }

    public UserProfile(ProfileConnection connection) {
        this.connections = List.of(connection);
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

    public List<UserInterest> getInterests() {
        return interests;
    }

    public void addInterest(UserInterest interest) {
        this.getInterests().add(interest);
    }

    public List<UserPrompt> getPrompts() {
        return prompts;
    }

    public void addPrompt(UserPrompt prompt) {
        prompt.setUserProfile(this);

        this.getPrompts().add(prompt);
    }

    public List<ProfileConnection> getConnections() {
        return connections;
    }

    public void addConnection(ProfileConnection connection) {
        this.getConnections().add(connection);
    }

    public void setConnections(List<ProfileConnection> connections) {
        this.connections = connections;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", about='" + about + '\'' +
            ", picture=" + picture +
            ", user=" + user +
            ", medias=" + medias +
            ", interests=" + interests +
            ", prompts=" + prompts +
            ", connections=" + connections +
            '}';
    }
}