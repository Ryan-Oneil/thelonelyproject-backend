package org.lonelyproject.backend.service;

import static org.lonelyproject.backend.security.SecurityConstants.JWT_ROLE_KEY;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.HashMap;
import java.util.Map;
import org.lonelyproject.backend.api.BackBlazeAPI;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserDto;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.lonelyproject.backend.entities.ProfilePicture;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.enums.UserRole;
import org.lonelyproject.backend.exception.ProfileAlreadyRegistered;
import org.lonelyproject.backend.repository.ProfilePictureRepository;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.repository.UserRepository;
import org.lonelyproject.backend.security.UserAuth;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfilePictureRepository pictureRepository;
    private final ModelMapper mapper;
    private final BackBlazeAPI backBlazeAPI;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository,
        ProfilePictureRepository pictureRepository, ModelMapper mapper,
        BackBlazeAPI backBlazeAPI) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.pictureRepository = pictureRepository;
        this.mapper = mapper;
        this.backBlazeAPI = backBlazeAPI;
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    public UserProfileDto getPublicUserProfile(String userId) {
        UserProfile userProfile = getUserProfile(userId);

        return userProfileToDTO(userProfile);
    }

    public void updateProfileAbout(String userId, String about) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.setAbout(about);

        userProfileRepository.save(userProfile);
    }

    public void registerNewUser(UserProfileDto userProfileDto, UserAuth userAuth) throws FirebaseAuthException {
        if (userRepository.existsById(userAuth.getId())) {
            throw new ProfileAlreadyRegistered("Profile is already setup");
        }
        User user = new User(userAuth.getId(), userAuth.getUsername(), UserRole.ROLE_USER);
        UserProfile userProfile = new UserProfile(userProfileDto.getName(), userProfileDto.getAbout(), user);

        userProfileRepository.save(userProfile);
        setUserRole(userAuth.getId(), user.getRole());
    }

    public void setUserRole(String userId, UserRole role) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_ROLE_KEY, role.toString());

        FirebaseAuth.getInstance().setCustomUserClaims(userId, claims);
    }

    public String setUserProfilePicture(UploadedFile profilePicture, UserAuth userAuth) {
        UserProfile userProfile = getUserProfile(userAuth.getId());

        if (userProfile.getPicture() != null) {
            deleteUserProfilePicture(userProfile);
        }
        ProfilePicture picture = backBlazeAPI.uploadToProfileBucket(profilePicture);

        userProfile.setPicture(picture);
        userProfileRepository.save(userProfile);

        return picture.getUrl();
    }

    public void deleteUserProfilePicture(UserProfile userProfile) {
        ProfilePicture picture = userProfile.getPicture();
        CloudItemDetails details = picture.getItemDetails();

        backBlazeAPI.deleteFromBucket(details.getName(), details.getExternalId());
        pictureRepository.delete(picture);
    }

    public UserProfileDto userProfileToDTO(UserProfile userProfile) {
        return mapper.map(userProfile, UserProfileDto.class);
    }

    public UserDto userToDTO(User user) {
        return mapper.map(user, UserDto.class);
    }
}
