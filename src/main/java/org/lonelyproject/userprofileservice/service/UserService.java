package org.lonelyproject.userprofileservice.service;

import static org.lonelyproject.auth.config.SecurityConstants.JWT_ROLE_KEY;
import static org.lonelyproject.userprofileservice.util.ClassMapperUtil.mapClass;
import static org.lonelyproject.userprofileservice.util.ClassMapperUtil.mapList;
import static org.lonelyproject.userprofileservice.util.ClassMapperUtil.mapListIgnoreLazyCollection;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.lonelyproject.auth.dto.UserAuth;
import org.lonelyproject.userprofileservice.api.BackBlazeAPI;
import org.lonelyproject.userprofileservice.api.SpotifyAPI;
import org.lonelyproject.userprofileservice.dto.InterestCategoryDto;
import org.lonelyproject.userprofileservice.dto.ProfileConnectionDto;
import org.lonelyproject.userprofileservice.dto.ProfileMediaDto;
import org.lonelyproject.userprofileservice.dto.ProfileTraitDto;
import org.lonelyproject.userprofileservice.dto.PromptDto;
import org.lonelyproject.userprofileservice.dto.SpotifyArtistDto;
import org.lonelyproject.userprofileservice.dto.UploadedFile;
import org.lonelyproject.userprofileservice.dto.UserProfileDto;
import org.lonelyproject.userprofileservice.entities.Artist;
import org.lonelyproject.userprofileservice.entities.CloudItemDetails;
import org.lonelyproject.userprofileservice.entities.Genre;
import org.lonelyproject.userprofileservice.entities.Interest;
import org.lonelyproject.userprofileservice.entities.InterestCategory;
import org.lonelyproject.userprofileservice.entities.ProfileConnection;
import org.lonelyproject.userprofileservice.entities.ProfileMedia;
import org.lonelyproject.userprofileservice.entities.ProfilePicture;
import org.lonelyproject.userprofileservice.entities.Prompt;
import org.lonelyproject.userprofileservice.entities.User;
import org.lonelyproject.userprofileservice.entities.UserInterest;
import org.lonelyproject.userprofileservice.entities.UserProfile;
import org.lonelyproject.userprofileservice.entities.UserPrompt;
import org.lonelyproject.userprofileservice.entities.UserSpotifyArtist;
import org.lonelyproject.userprofileservice.entities.supers.CloudItem;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTraitRelation;
import org.lonelyproject.userprofileservice.enums.ConnectionStatus;
import org.lonelyproject.userprofileservice.enums.MediaType;
import org.lonelyproject.userprofileservice.enums.UserRole;
import org.lonelyproject.userprofileservice.exception.ProfileException;
import org.lonelyproject.userprofileservice.exception.ResourceNotFound;
import org.lonelyproject.userprofileservice.repository.CloudItemRepository;
import org.lonelyproject.userprofileservice.repository.ProfileMatchRepository;
import org.lonelyproject.userprofileservice.repository.ProfileTraitRepository;
import org.lonelyproject.userprofileservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final CloudItemRepository<CloudItem> cloudItemRepository;
    private final ProfileTraitRepository<ProfileTraitRelation> profileTraitRepository;
    private final ProfileMatchRepository profileMatchRepository;
    private final BackBlazeAPI backBlazeAPI;
    private final String cdnUrl;

    public UserService(UserProfileRepository userProfileRepository, CloudItemRepository<CloudItem> cloudItemRepository,
        ProfileTraitRepository<ProfileTraitRelation> profileTraitRepository, ProfileMatchRepository profileMatchRepository, BackBlazeAPI backBlazeAPI,
        @Value("${cdn.url}") String cdnUrl) {
        this.userProfileRepository = userProfileRepository;
        this.cloudItemRepository = cloudItemRepository;
        this.profileTraitRepository = profileTraitRepository;
        this.profileMatchRepository = profileMatchRepository;
        this.backBlazeAPI = backBlazeAPI;
        this.cdnUrl = cdnUrl;
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new ProfileException("User doesn't exist"));
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

    public void addInterestToUserProfile(String userId, ProfileTraitDto interestDto) {
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
            .flatMap(connection -> Stream.of(connection.getTarget(), connection.getConnector()))
            .filter(profile -> !profile.getId().equals(userId))
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

    public List<SpotifyArtistDto> integrateSpotifyWithProfile(String userAuth, String spotifyToken) {
        UserProfile userProfile = getUserProfile(userAuth);
        List<SpotifyArtistDto> artistDtos = SpotifyAPI.fetchArtist(spotifyToken);

        List<UserSpotifyArtist> profileArtists = artistDtos.stream()
            .map(spotifyArtistDto -> new UserSpotifyArtist(userProfile, getOrCreateArtist(spotifyArtistDto))).toList();

        profileTraitRepository.deleteAllUserSpotifyArtists(userAuth);
        profileTraitRepository.saveAll(profileArtists);

        return artistDtos;
    }

    public Artist getOrCreateArtist(SpotifyArtistDto spotifyArtistDto) {
        return profileTraitRepository.getArtistByName(spotifyArtistDto.getName()).orElseGet(() -> {
            Set<Genre> genres = spotifyArtistDto.getGenres().stream().map(this::getOrCreateGenre).collect(Collectors.toSet());

            return new Artist(spotifyArtistDto.getName(), spotifyArtistDto.getImages().get(0).get("url").toString(), genres);
        });
    }

    public Genre getOrCreateGenre(String genreName) {
        return profileTraitRepository.getGenreByName(genreName).orElseGet(() -> new Genre(genreName));
    }

    public String getCdnUrl(CloudItemDetails cloudItemDetails) {
        return "%s/%s/%s".formatted(cdnUrl, cloudItemDetails.getContainerName(), cloudItemDetails.getName());
    }

}
