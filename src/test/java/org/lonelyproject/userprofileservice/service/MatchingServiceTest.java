package org.lonelyproject.userprofileservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lonelyproject.userprofileservice.dto.UserProfileDto;
import org.lonelyproject.userprofileservice.entities.ProfileMatch;
import org.lonelyproject.userprofileservice.entities.UserProfile;
import org.lonelyproject.userprofileservice.exception.ProfileException;
import org.springframework.beans.factory.annotation.Autowired;

class MatchingServiceTest extends BaseServiceTest {

    @Autowired
    private MatchingService matchingService;

    @Test
    void getUserProfileValidTest() {
        UserProfile userProfile = matchingService.getUserProfile("1");

        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getId()).isEqualTo("1");
    }

    @Test
    void getUserProfileInValidTest() {
        RuntimeException exception = Assertions.assertThrows(ProfileException.class, () -> matchingService.getUserProfile("abcd"));
        assertThat(exception.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    @Transactional
    void getMatchesEmptyTest() {
        List<UserProfileDto> profileDtos = matchingService.getMatches("4");

        assertThat(profileDtos).isEmpty();
    }

    @Test
    @Transactional
    void getMatchesTest() {
        List<UserProfileDto> profileDtos = matchingService.getMatches("1");

        assertThat(profileDtos).isNotEmpty();
    }

    @Test
    void getNewMatchPotentialForUserEmptyTest() {
        UserProfile userProfile = matchingService.getUserProfile("1");
        List<UserProfile> profiles = matchingService.getNewMatchPotentialForUser(
            List.of(new ProfileMatch(0d, userProfile, new UserProfile("4")), new ProfileMatch(0d, userProfile, new UserProfile("5"))),
            userProfile);

        assertThat(profiles).isEmpty();
    }

    @Test
    void getNewMatchPotentialForUserNotEmptyTest() {
        UserProfile userProfile = matchingService.getUserProfile("2");
        List<UserProfile> profiles = matchingService.getNewMatchPotentialForUser(new ArrayList<>(), userProfile);

        assertThat(profiles).isNotEmpty();
    }

    @Test
    @Transactional
    void calculateMatchScoreTest() {
        UserProfile userProfile1 = matchingService.getUserProfile("1");
        UserProfile userProfile2 = matchingService.getUserProfile("2");

        ProfileMatch profileMatch = matchingService.calculateMatchScore(userProfile1, userProfile2);

        assertThat(profileMatch.getScore()).isEqualTo(2.0);
    }

    @Test
    @Transactional
    void calculateMatchScoreNoScoreTest() {
        UserProfile userProfile1 = matchingService.getUserProfile("1");
        UserProfile userProfile2 = matchingService.getUserProfile("5");

        ProfileMatch profileMatch = matchingService.calculateMatchScore(userProfile1, userProfile2);

        assertThat(profileMatch.getScore()).isZero();
    }
}
