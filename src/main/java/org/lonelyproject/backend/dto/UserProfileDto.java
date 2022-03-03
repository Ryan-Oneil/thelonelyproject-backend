package org.lonelyproject.backend.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class UserProfileDto implements Serializable {

    private String userId;
    private String name;
    private String about;
    private String profilePictureUrl;
    private List<ProfileMediaDto> medias = new ArrayList<>();
    private List<InterestDto> interests = new ArrayList<>();
    private List<PromptDto> prompts = new ArrayList<>();
    private List<String> spotifyArtists = new ArrayList<>();

    public UserProfileDto() {
    }

    public UserProfileDto(String userId, String name, String about, String profilePictureUrl) {
        this.userId = userId;
        this.name = name;
        this.about = about;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<ProfileMediaDto> getMedias() {
        return medias;
    }

    public void setMedias(List<ProfileMediaDto> medias) {
        this.medias = medias;
    }

    public List<InterestDto> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestDto> interests) {
        this.interests = interests;
    }

    public List<PromptDto> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<PromptDto> prompts) {
        this.prompts = prompts;
    }

    public List<String> getSpotifyArtists() {
        return spotifyArtists;
    }

    public void setSpotifyArtists(List<String> spotifyArtists) {
        this.spotifyArtists = spotifyArtists;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
            "userId='" + userId + '\'' +
            ", name='" + name + '\'' +
            ", about='" + about + '\'' +
            ", profilePictureUrl='" + profilePictureUrl + '\'' +
            ", medias=" + medias +
            ", interests=" + interests +
            ", prompts=" + prompts +
            ", spotifyArtists=" + spotifyArtists +
            '}';
    }
}
