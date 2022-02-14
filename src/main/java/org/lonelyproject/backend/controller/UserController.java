package org.lonelyproject.backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.backend.service.FileService;
import org.lonelyproject.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/profile")
    public UserProfileDto getUserProfile(@AuthenticationPrincipal UserAuth auth) {
        return userService.userProfileToDTO(userService.getUserProfile(auth.getId()));
    }

    @PostMapping("/register")
    public void setUpUserProfile(@RequestBody UserProfileDto userProfile, @AuthenticationPrincipal UserAuth auth) throws FirebaseAuthException {
        userService.registerNewUser(userProfile, auth);
    }

    @PostMapping("/uploadPicture")
    public String uploadUserProfilePicture(HttpServletRequest request, @AuthenticationPrincipal UserAuth auth) throws IOException {
        UploadedFile profilePicture = fileService.handleFileUpload(request, -1).get(0);

        return userService.setUserProfilePicture(profilePicture, auth);
    }
}
