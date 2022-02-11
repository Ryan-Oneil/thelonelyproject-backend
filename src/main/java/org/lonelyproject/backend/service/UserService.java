package org.lonelyproject.backend.service;

import static org.lonelyproject.backend.security.SecurityConstants.JWT_ROLE_KEY;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.HashMap;
import java.util.Map;
import org.lonelyproject.backend.dto.UserDto;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.entities.User;
import org.lonelyproject.backend.entities.UserProfile;
import org.lonelyproject.backend.enums.UserRole;
import org.lonelyproject.backend.repository.UserProfileRepository;
import org.lonelyproject.backend.repository.UserRepository;
import org.lonelyproject.backend.security.UserAuth;
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

    public void registerNewUser(UserProfileDto userProfileDto, UserAuth userAuth) throws FirebaseAuthException {
        User user = new User(userAuth.getId(), userAuth.getUsername(), UserRole.ROLE_USER);
        UserProfile userProfile = new UserProfile(userProfileDto.getName(), userProfileDto.getAbout(), user);

        userProfileRepository.save(userProfile);
        setUserRole(userAuth.getId(), user.getRole());
    }

    public void setUserRole(String userId, UserRole role) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_ROLE_KEY, role);

        FirebaseAuth.getInstance().setCustomUserClaims(userId, claims);
    }

    public UserProfileDto userProfileToDTO(UserProfile userProfile) {
        return mapper.map(userProfile, UserProfileDto.class);
    }

    public UserDto userToDTO(User user) {
        return mapper.map(user, UserDto.class);
    }
}
