package org.lonelyproject.userprofileservice.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.lonelyproject.userprofileservice.dto.UserProfileDto;
import org.lonelyproject.userprofileservice.entities.Interest;
import org.lonelyproject.userprofileservice.entities.InterestCategory;
import org.lonelyproject.userprofileservice.entities.ProfileMatch;
import org.lonelyproject.userprofileservice.entities.UserInterest;
import org.lonelyproject.userprofileservice.entities.UserProfile;
import org.lonelyproject.userprofileservice.enums.ConnectionStatus;
import org.lonelyproject.userprofileservice.exception.ProfileException;
import org.lonelyproject.userprofileservice.repository.ProfileMatchRepository;
import org.lonelyproject.userprofileservice.repository.UserProfileRepository;
import org.lonelyproject.userprofileservice.util.ClassMapperUtil;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    private final UserProfileRepository userProfileRepository;
    private final ProfileMatchRepository matchRepository;

    public MatchingService(UserProfileRepository userProfileRepository, ProfileMatchRepository matchRepository) {
        this.userProfileRepository = userProfileRepository;
        this.matchRepository = matchRepository;
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new ProfileException("User doesn't exist"));
    }

    public List<UserProfileDto> getMatches(String userId) {
        UserProfile userProfile = getUserProfile(userId);

        List<ProfileMatch> existingMatches = matchRepository.getALlMatchesByUser(userId);
        List<UserProfile> profiles = getNewMatchPotentialForUser(existingMatches, userProfile);

        List<ProfileMatch> newMatches = profiles.stream()
            .map(profile -> calculateMatchScore(userProfile, profile))
            .toList();
        matchRepository.saveAll(newMatches);

        List<UserProfile> matches = Stream.of(existingMatches, newMatches)
            .flatMap(Collection::stream)
            .sorted(Comparator.comparingDouble(ProfileMatch::getScore).reversed())
            .flatMap(profileMatch -> Stream.of(profileMatch.getUserProfile(), profileMatch.getMatchUserProfile()))
            .filter(profile -> !profile.getId().equals(userId))
            .toList();

        return ClassMapperUtil.mapListIgnoreLazyCollection(matches, UserProfileDto.class);
    }

    public List<UserProfile> getNewMatchPotentialForUser(List<ProfileMatch> existingMatchProfile, UserProfile userProfile) {
        List<String> existingConnections = userProfileRepository.getAllMatchesByConnector(userProfile.getId()).stream()
            .map(UserProfile::getId)
            .toList();

        List<String> profileIds = existingMatchProfile.stream()
            .flatMap(profileMatch -> Stream.of(profileMatch.getMatchId().getMatchProfileId(), profileMatch.getMatchId().getProfileId()))
            .distinct()
            .collect(Collectors.toList());

        profileIds.add(userProfile.getId());
        profileIds.addAll(existingConnections);

        return userProfileRepository.findAllNotInList(profileIds);
    }

    public ProfileMatch calculateMatchScore(UserProfile userProfile, UserProfile potentialMatch) {
        double score = calculateInterestSimilarity(userProfile, potentialMatch);
        score += calculatePreviousMatchesSimilarity(userProfile, potentialMatch);

        return new ProfileMatch(score, userProfile, potentialMatch);
    }

    public double calculateInterestSimilarity(UserProfile userProfile, UserProfile potentialMatch) {
        List<Interest> interests = userProfile.getInterests().stream().map(UserInterest::getInterest).toList();
        List<InterestCategory> categories = interests.stream().map(Interest::getCategory).toList();

        return potentialMatch.getInterests().stream().mapToDouble(value -> {
            if (interests.contains(value.getInterest())) {
                return 1;
            }

            if (categories.contains(value.getInterest().getCategory())) {
                return 0.5;
            }
            return 0;
        }).sum();
    }

    public double calculatePreviousMatchesSimilarity(UserProfile userProfile, UserProfile potentialMatch) {
        List<UserProfile> matches = userProfile.getConnections().stream()
            .flatMap(connection -> Stream.of(connection.getConnector(), connection.getTarget()))
            .distinct()
            .filter(profile -> !profile.getId().equals(userProfile.getId()))
            .toList();

        return potentialMatch.getConnections().stream()
            .filter(connection -> !connection.getStatus().equals(ConnectionStatus.DENIED))
            .mapToDouble(value -> {
                if (matches.contains(value.getConnector()) || matches.contains(value.getTarget())) {
                    return value.getStatus().equals(ConnectionStatus.CONNECTED) ? 1 : 0.5;
                }
                return 0;
            }).sum();
    }

}
