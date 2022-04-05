package org.lonelyproject.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.entities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

class UserProfileRepositoryTest extends BaseRepository {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void getUserProfileByUserIdValidTest() {
        Optional<UserProfile> profile = userProfileRepository.getUserProfileByUserId("1");

        assertThat(profile).isPresent();
        assertThat(profile.get().getId()).isEqualTo("1");
    }

    @Test
    void getUserProfileByUserIdInValidTest() {
        Optional<UserProfile> profile = userProfileRepository.getUserProfileByUserId("abc");

        assertThat(profile).isNotPresent();
    }

    @Test
    void findAllTest() {
        List<UserProfile> profiles = userProfileRepository.findAll();

        assertThat(profiles).isNotEmpty();
    }

    @Test
    void hasConnectionValidTest() {
        boolean hasConnection = userProfileRepository.hasConnection("1", "2");

        assertThat(hasConnection).isTrue();
    }

    @Test
    void hasConnectionInValidTest() {
        boolean hasConnection = userProfileRepository.hasConnection("2", "3");

        assertThat(hasConnection).isFalse();
    }

    @Test
    void getProfileConnectionValidTest() {
        Optional<UserProfile> profile = userProfileRepository.getProfileConnection("1", "2");

        assertThat(profile).isPresent();
    }

    @Test
    void getProfileConnectionInValidTest() {
        Optional<UserProfile> profile = userProfileRepository.getProfileConnection("2", "3");

        assertThat(profile).isNotPresent();
    }

    @Test
    void isConnectedValidTest() {
        boolean isConnected = userProfileRepository.isConnected("1", "3");

        assertThat(isConnected).isTrue();
    }

    @Test
    void isConnectedInValidTest() {
        boolean isConnected = userProfileRepository.isConnected("1", "4");

        assertThat(isConnected).isFalse();
    }

    @Test
    void findAllNotInListTest() {
        List<UserProfile> profiles = userProfileRepository.findAllNotInList(List.of("1", "2"));

        assertThat(profiles).isNotEmpty();
        assertThat(profiles.size()).isEqualTo(3);
    }

    @Test
    void getAllMatchesByConnectorTest() {
        List<UserProfile> profiles = userProfileRepository.getAllMatchesByConnector("1");

        assertThat(profiles).isNotEmpty();
        assertThat(profiles.size()).isEqualTo(2);
    }

    @Test
    void getAllMatchesByConnectorNoneTest() {
        List<UserProfile> profiles = userProfileRepository.getAllMatchesByConnector("3");

        assertThat(profiles).isEmpty();
    }
}
