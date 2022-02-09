package org.lonelyproject.backend.service;

import org.lonelyproject.backend.dto.UserDto;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ModelMapper mapper;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.mapper = mapper;
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.getUserProfileByUserId(userId).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    public UserProfileDto userProfileToDTO(UserProfile userProfile) {
        return mapper.map(userProfile, UserProfileDto.class);
    }

    public UserDto userToDTO(User user) {
        return mapper.map(user, UserDto.class);
    }
}
