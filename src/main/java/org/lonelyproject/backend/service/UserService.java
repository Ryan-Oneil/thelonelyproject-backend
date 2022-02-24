package org.lonelyproject.backend.service;

import static org.lonelyproject.backend.security.SecurityConstants.JWT_ROLE_KEY;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lonelyproject.backend.api.BackBlazeAPI;
import org.lonelyproject.backend.dto.InterestCategoryDto;
import org.lonelyproject.backend.dto.ProfileMediaDto;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.lonelyproject.backend.entities.InterestCategory;
import org.lonelyproject.backend.entities.ProfileMedia;
import org.lonelyproject.backend.entities.ProfilePicture;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.enums.MediaType;
import org.lonelyproject.backend.enums.UserRole;
import org.lonelyproject.backend.exception.ProfileAlreadyRegistered;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.repository.InterestCategoryRepository;
import org.lonelyproject.backend.repository.ProfileMediaRepository;
import org.lonelyproject.backend.repository.ProfilePictureRepository;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.repository.UserRepository;
import org.lonelyproject.backend.security.UserAuth;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfilePictureRepository pictureRepository;
    private final ProfileMediaRepository mediaRepository;
    private final InterestCategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final BackBlazeAPI backBlazeAPI;
    private final String cdnUrl;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository,
        ProfilePictureRepository pictureRepository, ProfileMediaRepository mediaRepository,
        InterestCategoryRepository categoryRepository, BackBlazeAPI backBlazeAPI, @Value("${cdn.url}") String cdnUrl) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.pictureRepository = pictureRepository;
        this.mediaRepository = mediaRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = new ModelMapper();
        this.backBlazeAPI = backBlazeAPI;
        this.cdnUrl = cdnUrl;

        this.mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    public UserProfileDto getPublicUserProfile(String userId) {
        UserProfile userProfile = getUserProfile(userId);
        List<ProfileMedia> medias = mediaRepository.getAllByUserProfile_IdOrderByIdDesc(userId);
        userProfile.setMedias(medias);

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
        CloudItemDetails cloudItemDetails = backBlazeAPI.uploadToProfileBucket(profilePicture);
        ProfilePicture picture = new ProfilePicture(cloudItemDetails, getCdnUrl(cloudItemDetails));

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

    public List<ProfileMediaDto> addMediaToUserProfileGallery(String userId, List<UploadedFile> uploadedFiles) {
        List<ProfileMedia> medias = uploadedFiles.stream()
            .map(uploadedFile -> {
                CloudItemDetails details = backBlazeAPI.uploadToProfileBucket(uploadedFile);

                return new ProfileMedia(details, getCdnUrl(details), MediaType.IMAGE, new UserProfile(userId));
            }).toList();
        mediaRepository.saveAll(medias);

        return mapList(medias, ProfileMediaDto.class);
    }

    public void deleteProfileMedia(int mediaId, String userId) {
        ProfileMedia media = mediaRepository.findByIdAndUserProfile_Id(mediaId, userId)
            .orElseThrow(() -> new ResourceNotFound("This media doesn't exist"));

        CloudItemDetails details = media.getItemDetails();

        backBlazeAPI.deleteFromBucket(details.getName(), details.getExternalId());
        mediaRepository.delete(media);
    }

    public List<InterestCategoryDto> getInterestsByCategory() {
        List<InterestCategory> categories = categoryRepository.findAll();

        return mapList(categories, InterestCategoryDto.class);
    }

    public String getCdnUrl(CloudItemDetails cloudItemDetails) {
        return "%s/%s/%s".formatted(cdnUrl, cloudItemDetails.getContainerName(), cloudItemDetails.getName());
    }

    public UserProfileDto userProfileToDTO(UserProfile userProfile) {
        return mapper.map(userProfile, UserProfileDto.class);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
            .stream()
            .map(element -> mapper.map(element, targetClass))
            .toList();
    }

}
