package org.lonelyproject.backend.dto;

public final class UserDto {

    private String id;
    private String email;

    public UserDto() {
    }

    public UserDto(String uuid, String email) {
        this.id = uuid;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}