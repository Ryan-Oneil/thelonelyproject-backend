package org.lonelyproject.userprofileservice.dto;

import java.io.Serializable;
import org.lonelyproject.userprofileservice.enums.MediaType;

public class ProfileMediaDto implements Serializable {

    private int id;
    private String url;
    private MediaType type;

    public ProfileMediaDto() {
    }

    public ProfileMediaDto(int id, String url, MediaType type) {
        this.id = id;
        this.url = url;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserProfileGalleryMediaDto{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", type=" + type +
            '}';
    }
}
