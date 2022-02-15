package org.lonelyproject.backend.dto;

public final class UserProfileDto {

    private String userId;
    private String name;
    private String about;
    private String profilePictureUrl;

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

    @Override
    public String toString() {
        return "UserProfileDto{" +
            "userId='" + userId + '\'' +
            ", name='" + name + '\'' +
            ", about='" + about + '\'' +
            ", profilePictureUrl='" + profilePictureUrl + '\'' +
            '}';
    }
}
