package org.lonelyproject.backend.service;

import static org.lonelyproject.backend.security.SecurityConstants.JWT_ROLE_KEY;
import static org.lonelyproject.backend.util.ClassMapperUtil.mapClass;
import static org.lonelyproject.backend.util.ClassMapperUtil.mapList;
import static org.lonelyproject.backend.util.ClassMapperUtil.mapListIgnoreLazyCollection;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.lonelyproject.backend.api.BackBlazeAPI;
import org.lonelyproject.backend.dto.InterestCategoryDto;
import org.lonelyproject.backend.dto.InterestDto;
import org.lonelyproject.backend.dto.ProfileConnectionDto;
import org.lonelyproject.backend.dto.ProfileMediaDto;
import org.lonelyproject.backend.dto.PromptDto;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.lonelyproject.backend.entities.Interest;
import org.lonelyproject.backend.entities.InterestCategory;
import org.lonelyproject.backend.entities.ProfileConnection;
import org.lonelyproject.backend.entities.ProfileMedia;
import org.lonelyproject.backend.entities.ProfilePicture;
import org.lonelyproject.backend.entities.Prompt;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.entities.UserInterest;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.entities.UserPrompt;
import org.lonelyproject.backend.entities.supers.CloudItem;
import org.lonelyproject.backend.entities.supers.ProfileTrait;
import org.lonelyproject.backend.enums.ConnectionStatus;
import org.lonelyproject.backend.enums.MediaType;
import org.lonelyproject.backend.enums.UserRole;
import org.lonelyproject.backend.exception.ProfileException;
import org.lonelyproject.backend.exception.ResourceNotFound;
import org.lonelyproject.backend.repository.CloudItemRepository;
import org.lonelyproject.backend.repository.ProfileMatchRepository;
import org.lonelyproject.backend.repository.ProfileTraitRepository;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.security.UserAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final CloudItemRepository<CloudItem> cloudItemRepository;
    private final ProfileTraitRepository<ProfileTrait> profileTraitRepository;
    private final ProfileMatchRepository profileMatchRepository;
    private final BackBlazeAPI backBlazeAPI;
    private final String cdnUrl;

    public UserService(UserProfileRepository userProfileRepository, CloudItemRepository<CloudItem> cloudItemRepository,
        ProfileTraitRepository<ProfileTrait> profileTraitRepository, ProfileMatchRepository profileMatchRepository, BackBlazeAPI backBlazeAPI,
        @Value("${cdn.url}") String cdnUrl) {
        this.userProfileRepository = userProfileRepository;
        this.cloudItemRepository = cloudItemRepository;
        this.profileTraitRepository = profileTraitRepository;
        this.profileMatchRepository = profileMatchRepository;
        this.backBlazeAPI = backBlazeAPI;
        this.cdnUrl = cdnUrl;
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    public UserProfileDto getPublicUserProfile(String userId, String requester) {
        UserProfile userProfile = getUserProfile(userId);
        Optional<UserProfile> connector = userProfileRepository.getProfileConnection(userId, requester);

        UserProfileDto profileDto = mapClass(userProfile, UserProfileDto.class);
        connector.ifPresent(profile -> {
            ProfileConnection profileConnection = profile.getConnections().get(0);
            ProfileConnectionDto profileConnectionDto = new ProfileConnectionDto(profileConnection.getStatus());

            if (profileConnection.getStatus() != ConnectionStatus.CONNECTED) {
                profileConnectionDto.setAttemptingToConnect(true);
                profileConnectionDto.setConnector(profileConnection.getConnector().getId().equals(requester));
            }
            profileDto.setConnection(profileConnectionDto);
        });
        return profileDto;
    }

    public List<UserProfileDto> getProfiles() {
        List<UserProfile> profiles = userProfileRepository.findAll();

        return mapListIgnoreLazyCollection(profiles, UserProfileDto.class);
    }

    public void updateProfileAbout(String userId, String about) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.setAbout(about);

        userProfileRepository.save(userProfile);
    }

    public void registerNewUser(UserProfileDto userProfileDto, UserAuth userAuth) throws FirebaseAuthException {
        if (userProfileRepository.existsById(userAuth.getId())) {
            throw new ProfileException("Profile is already setup");
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
        cloudItemRepository.delete(picture);
    }

    public List<ProfileMediaDto> addMediaToUserProfileGallery(String userId, List<UploadedFile> uploadedFiles) {
        List<ProfileMedia> medias = uploadedFiles.stream()
            .map(uploadedFile -> {
                CloudItemDetails details = backBlazeAPI.uploadToProfileBucket(uploadedFile);

                return new ProfileMedia(details, getCdnUrl(details), MediaType.IMAGE, new UserProfile(userId));
            }).toList();
        cloudItemRepository.saveAll(medias);

        return mapList(medias, ProfileMediaDto.class);
    }

    public void deleteProfileMedia(int mediaId, String userId) {
        ProfileMedia media = cloudItemRepository.findProfileMediaByIdAndUserProfile_Id(mediaId, userId)
            .orElseThrow(() -> new ResourceNotFound("This media doesn't exist"));

        CloudItemDetails details = media.getItemDetails();

        backBlazeAPI.deleteFromBucket(details.getName(), details.getExternalId());
        cloudItemRepository.delete(media);
    }

    public List<InterestCategoryDto> getInterestsByCategory() {
        List<InterestCategory> categories = profileTraitRepository.findAllInterestCategories();

        return mapList(categories, InterestCategoryDto.class);
    }

    public void addInterestToUserProfile(String userId, InterestDto interestDto) {
        if (profileTraitRepository.getTotalProfileInterests(userId) >= 8) {
            throw new ProfileException("You can only have 8 interests");
        }
        Interest interest = profileTraitRepository.getInterestById(interestDto.getId()).orElseThrow(() -> new ResourceNotFound("Invalid Interest"));
        UserProfile userProfile = getUserProfile(userId);
        userProfile.addInterest(new UserInterest(userProfile, interest));

        userProfileRepository.save(userProfile);
    }

    public void deleteUserProfileInterest(String userId, int interestId) {
        profileTraitRepository.deleteUserInterestById(interestId, userId);
    }

    public void addPromptToUserProfile(String userId, PromptDto promptDto) {
        Prompt prompt = profileTraitRepository.getPromptById(promptDto.getPromptId()).orElseThrow(() -> new ResourceNotFound("Invalid Prompt"));
        UserProfile userProfile = getUserProfile(userId);
        userProfile.addPrompt(new UserPrompt(userProfile, promptDto.getText(), prompt));

        userProfileRepository.save(userProfile);
    }

    public void deleteUserProfilePrompt(String userId, int promptId) {
        profileTraitRepository.deleteUserPromptById(promptId, userId);
    }

    public List<PromptDto> getPrompts() {
        return mapList(profileTraitRepository.findAllPrompts(), PromptDto.class);
    }

    public void editUserPrompt(String userId, PromptDto promptDto) {
        UserProfile userProfile = getUserProfile(userId);
        UserPrompt prompt = userProfile.getPrompts().stream()
            .filter(userPrompt -> userPrompt.getPrompt().getId() == promptDto.getPromptId())
            .findFirst()
            .orElseThrow(() -> new ResourceNotFound("You don't have this prompt"));
        prompt.setText(promptDto.getText());

        userProfileRepository.save(userProfile);
    }

    public void sendConnectionRequest(String connectorId, String targetId) {
        if (userProfileRepository.hasConnection(connectorId, targetId)) {
            throw new ProfileException("You already have an established or pending connection with this person");
        }
        UserProfile targetProfile = getUserProfile(targetId);

        ProfileConnection profileConnection = new ProfileConnection(ConnectionStatus.PENDING, new UserProfile(connectorId), targetProfile);
        targetProfile.addSentConnections(profileConnection);

        userProfileRepository.save(targetProfile);
        profileMatchRepository.deleteByMatchAndTarget(connectorId, targetId);
    }

    public List<UserProfileDto> getConnectionsByStatus(String userId, ConnectionStatus status) {
        UserProfile userProfile = getUserProfile(userId);
        List<ProfileConnection> connections = userProfile.getConnections();
        
        List<UserProfile> profiles = connections.stream()
            .filter(connection -> connection.getStatus() == status)
            .map(ProfileConnection::getConnector)
            .toList();

        return mapListIgnoreLazyCollection(profiles, UserProfileDto.class);
    }

    public void changeConnectionStatus(String targetId, String connectorId, ConnectionStatus status) {
        UserProfile userProfile = userProfileRepository.getProfileConnection(targetId, connectorId)
            .orElseThrow(() -> new ProfileException("You don't have a pending connection with this person"));

        ProfileConnection profileConnection = userProfile.getConnections().get(0);

        if (!profileConnection.getConnector().getId().equals(connectorId)) {
            throw new ProfileException("You can't accept your own request");
        }
        profileConnection.setStatus(status);

        userProfileRepository.save(profileConnection.getConnector());
        profileMatchRepository.deleteByMatchAndTarget(connectorId, targetId);
    }

    public String getCdnUrl(CloudItemDetails cloudItemDetails) {
        return "%s/%s/%s".formatted(cdnUrl, cloudItemDetails.getContainerName(), cloudItemDetails.getName());
    }

}
