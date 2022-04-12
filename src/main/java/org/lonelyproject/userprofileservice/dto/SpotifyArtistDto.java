package org.lonelyproject.userprofileservice.dto;

import java.util.HashMap;
import java.util.List;

public class SpotifyArtistDto {

    private String name;

    private List<String> genres;

    private List<HashMap<String, Object>> images;

    public SpotifyArtistDto() {
    }

    public SpotifyArtistDto(String name, List<String> genres, List<HashMap<String, Object>> images) {
        this.name = name;
        this.genres = genres;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<HashMap<String, Object>> getImages() {
        return images;
    }

    public void setImages(List<HashMap<String, Object>> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "SpotifyArtistDto{" +
            "name='" + name + '\'' +
            ", genres=" + genres +
            ", images=" + images +
            '}';
    }
}
