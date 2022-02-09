package org.lonelyproject.backend.dto;

public final class UserProfileDto {

    private Long id;
    private String name;
    private String about;
    private Boolean isSetup;
    private UserDto user;

    public UserProfileDto() {
    }

    public UserProfileDto(Long id, String name, String about, UserDto user) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Boolean getSetup() {
        return isSetup;
    }

    public void setSetup(Boolean setup) {
        isSetup = setup;
    }
}
