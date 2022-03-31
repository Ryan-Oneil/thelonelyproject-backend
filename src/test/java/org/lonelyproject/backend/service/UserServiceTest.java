package org.lonelyproject.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.api.BackBlazeAPI;
import org.lonelyproject.backend.dto.InterestCategoryDto;
import org.lonelyproject.backend.dto.InterestDto;
import org.lonelyproject.backend.dto.ProfileMediaDto;
import org.lonelyproject.backend.dto.PromptDto;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.enums.ConnectionStatus;
import org.lonelyproject.backend.exception.ProfileException;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.security.UserAuth;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class UserServiceTest extends BaseServiceTest {

    @MockBean
    private BackBlazeAPI backBlazeAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(backBlazeAPI.uploadToProfileBucket(ArgumentMatchers.any())).thenReturn(new CloudItemDetails("ase", "test.png", "test", 0L));
    }

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
        UserProfileDto userProfileDto = userService.getPublicUserProfile("4", "1");

        assertThat(userProfileDto.getConnection().isAttemptingToConnect()).isTrue();
        assertThat(userProfileDto.getConnection().getConnectionStatus()).hasToString("PENDING");
    }

    @Test
    @Transactional
    void getPublicUserProfileTest2() {
        UserProfileDto userProfileDto = userService.getPublicUserProfile("1", "2");

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

        ProfileException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.registerNewUser(userProfileDto, new UserAuth("1", "test", true, "ROLE_USER")));
        assertThat(exception.getMessage()).isEqualTo("Profile is already setup");
    }

    @Test
    void setUserProfilePictureTest() throws IOException {
        File file = new File("test.png");
        file.createNewFile();

        String url = userService.setUserProfilePicture(new UploadedFile(file.getName(), "", file), new UserAuth("1", "test", true, "ROLE_USER"));
        file.delete();

        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(url).isNotBlank();
        assertThat(userProfile.getPicture()).isNotNull();
        assertThat(userProfile.getPicture().getUrl()).isEqualTo(url);
    }

    @Test
    @Transactional
    void addMediaToUserProfileGalleryTest() throws IOException {
        File file = new File("test.png");
        file.createNewFile();

        List<UploadedFile> files = List.of(new UploadedFile(file.getName(), "", file));

        List<ProfileMediaDto> mediaDtos = userService.addMediaToUserProfileGallery("2", files);
        file.delete();
        UserProfile userProfile = userService.getUserProfile("2");

        assertThat(mediaDtos).isNotEmpty();
        assertThat(userProfile.getMedias().size()).isEqualTo(1);
    }

    @Test
    void getInterestsByCategoryTest() {
        List<InterestCategoryDto> interests = userService.getInterestsByCategory();

        assertThat(interests).isNotEmpty();
    }

    @Test
    @Transactional
    void addInterestToUserProfileTest() {
        InterestDto interestDto = new InterestDto(2, "test");
        userService.addInterestToUserProfile("1", interestDto);

        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(userProfile.getInterests().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void addInterestToUserProfileThrowsExceptionTest() {
        InterestDto interestDto = new InterestDto(9, "test9");

        ProfileException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.addInterestToUserProfile("3", interestDto));
        assertThat(exception.getMessage()).isEqualTo("You can only have 8 interests");
    }

    @Test
    @Transactional
    void addPromptToUserProfileTest() {
        PromptDto promptDto = new PromptDto("test", 1, "test");

        userService.addPromptToUserProfile("1", promptDto);
        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(userProfile.getPrompts()).isNotEmpty();
    }

    @Test
    void getPromptsTest() {
        List<PromptDto> prompts = userService.getPrompts();

        assertThat(prompts).isNotEmpty();
    }

    @Test
    @Transactional
    void editUserPromptTest() {
        PromptDto promptDto = new PromptDto("changedText", 1, "test");

        userService.editUserPrompt("1", promptDto);
        UserProfile userProfile = userService.getUserProfile("1");

        assertThat(userProfile.getPrompts().get(0).getText()).isEqualTo(promptDto.getText());
    }

    @Test
    @Transactional
    void sendConnectionRequestTest() {
        userService.sendConnectionRequest("2", "3");

        Optional<UserProfile> profile = userProfileRepository.getProfileConnection("3", "2");

        assertThat(profile).isPresent();
    }

    @Test
    void sendConnectionRequestAlreadyExistsTest() {
        ProfileException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.sendConnectionRequest("1", "2"));
        assertThat(exception.getMessage()).isEqualTo("You already have an established or pending connection with this person");
    }

    @Test
    @Transactional
    void getConnectionsByStatusTest() {
        List<UserProfileDto> profileDtos = userService.getConnectionsByStatus("1", ConnectionStatus.PENDING);

        assertThat(profileDtos.size()).isEqualTo(1);
    }

    @Test
    void changeConnectionStatusTest() {
        userService.changeConnectionStatus("2", "1", ConnectionStatus.CONNECTED);

        Optional<UserProfile> profile = userProfileRepository.getProfileConnection("1", "2");

        assertThat(profile).isPresent();
        assertThat(profile.get().getConnections()).isNotEmpty();
    }

    @Test
    void changeConnectionStatusInvalidTest() {
        ProfileException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.changeConnectionStatus("2", "90", ConnectionStatus.CONNECTED));
        assertThat(exception.getMessage()).isEqualTo("You don't have a pending connection with this person");
    }

    @Test
    void changeConnectionStatusInvalid2Test() {
        ProfileException exception = Assertions.assertThrows(ProfileException.class,
            () -> userService.changeConnectionStatus("1", "2", ConnectionStatus.CONNECTED));
        assertThat(exception.getMessage()).isEqualTo("You can't accept your own request");
    }
}
