package org.lonelyproject.backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.backend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileDto getUserProfile(Authentication authentication) {
        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        return userService.userProfileToDTO(userService.getUserProfile(userAuth.getId()));
    }

    @PostMapping("/register")
    public void setUpUserProfile(@RequestBody UserProfileDto userProfile, Authentication authentication) throws FirebaseAuthException {
        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        userService.registerNewUser(userProfile, userAuth);
    }
}
