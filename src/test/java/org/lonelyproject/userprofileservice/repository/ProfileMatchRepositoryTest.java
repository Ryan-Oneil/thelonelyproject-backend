package org.lonelyproject.userprofileservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.lonelyproject.userprofileservice.entities.ProfileMatch;
import org.lonelyproject.userprofileservice.entities.compositeids.MatchId;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileMatchRepositoryTest extends BaseRepository {

    @Autowired
    private ProfileMatchRepository profileMatchRepository;

    @Test
    void getALlMatchesByUserTest() {
        List<ProfileMatch> matches = profileMatchRepository.getALlMatchesByUser("1");

        assertThat(matches).isNotEmpty();
        assertThat(matches.size()).isEqualTo(2);
    }

    @Test
    void getALlMatchesByUserEmptyTest() {
        List<ProfileMatch> matches = profileMatchRepository.getALlMatchesByUser("2");

        assertThat(matches).isEmpty();
    }

    @Test
    void deleteByMatchAndTarget() {
        profileMatchRepository.deleteByMatchAndTarget("1", "2");

        boolean exists = profileMatchRepository.existsById(new MatchId("1", "2"));

        assertThat(exists).isFalse();
    }
}
