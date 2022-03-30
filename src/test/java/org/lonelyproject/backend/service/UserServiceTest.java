package org.lonelyproject.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.exception.ProfileException;
import org.lonelyproject.backend.security.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getUserProfileValidTest() {
        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getId()).isEqualTo("1");
    }

    @Test
    void getUserProfileInValidTest() {
        RuntimeException exception = Assertions.assertThrows(ProfileException.class, () -> userService.getUserProfile("abcd"));
        assertThat(exception.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    @Transactional
    void getPublicUserProfileTest() {
        UserProfileDto userProfileDto = userService.getPublicUserProfile("2", "1");

        assertThat(userProfileDto.getConnection().isAttemptingToConnect()).isTrue();
        assertThat(userProfileDto.getConnection().getConnectionStatus()).hasToString("PENDING");
    }

    @Test
    @Transactional
    void getPublicUserProfileTest2() {
        UserProfileDto userProfileDto = userService.getPublicUserProfile("3", "1");

        assertThat(userProfileDto.getConnection().isAttemptingToConnect()).isFalse();
        assertThat(userProfileDto.getConnection().getConnectionStatus()).hasToString("CONNECTED");
    }

    @Test
    void getProfilesTest() {
        List<UserProfileDto> profiles = userService.getProfiles();

        assertThat(profiles).isNotEmpty();
    }

    @Test
    void updateProfileAboutTest() {
        String newAbout = "Changed Test";

        userService.updateProfileAbout("1", "Changed Test");
        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(userProfile.getAbout()).isEqualTo(newAbout);
    }

    @Test
    void registerNewUserTest() {
        UserProfileDto userProfileDto = new UserProfileDto("1", "testCreation", "test", "");

        RuntimeException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.registerNewUser(userProfileDto, new UserAuth("1", "test", true, "ROLE_USER")));
        assertThat(exception.getMessage()).isEqualTo("Profile is already setup");
    }
}
