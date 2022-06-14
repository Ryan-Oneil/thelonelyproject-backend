package org.lonelyproject.userprofileservice.api;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lonelyproject.userprofileservice.dto.SpotifyArtistDto;
import org.lonelyproject.userprofileservice.exception.ProfileException;
import org.lonelyproject.userprofileservice.util.ClassMapperUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SpotifyAPI {

    private static final Logger logger = LogManager.getLogger(SpotifyAPI.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    private SpotifyAPI() {
    }

    public static List<SpotifyArtistDto> fetchArtist(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", authToken));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=6&offset=0",
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                });
            return ClassMapperUtil.mapList((List<Object>) response.getBody().get("items"), SpotifyArtistDto.class);
        } catch (Exception e) {
            logger.error("Error syncing spotify artists", e);
            throw new ProfileException("Failed to sync spotify");
        }
    }
}
