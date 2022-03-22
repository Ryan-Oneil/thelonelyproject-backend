package org.lonelyproject.backend.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.Interest;
import org.lonelyproject.backend.entities.InterestCategory;
import org.lonelyproject.backend.entities.ProfileMatch;
import org.lonelyproject.backend.entities.UserInterest;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.enums.ConnectionStatus;
import org.lonelyproject.backend.repository.ProfileMatchRepository;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.util.ClassMapperUtil;
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
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    public List<UserProfileDto> getMatches(String userId) {
        UserProfile userProfile = getUserProfile(userId);

        List<ProfileMatch> existingMatches = matchRepository.getALlMatchesByUser(userId);
        List<UserProfile> profiles = getNewMatchPotentialForUser(existingMatches, userId);

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

    public List<UserProfile> getNewMatchPotentialForUser(List<ProfileMatch> existingMatchProfile, String userId) {
        List<String> profileIds = existingMatchProfile.stream()
            .flatMap(profileMatch -> Stream.of(profileMatch.getMatchId().getMatchProfileId(), profileMatch.getMatchId().getProfileId()))
            .distinct()
            .collect(Collectors.toList());

        profileIds.add(userId);

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
