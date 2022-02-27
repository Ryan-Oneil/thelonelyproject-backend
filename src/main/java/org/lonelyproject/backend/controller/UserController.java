package org.lonelyproject.backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.lonelyproject.backend.dto.InterestCategoryDto;
import org.lonelyproject.backend.dto.InterestDto;
import org.lonelyproject.backend.dto.ProfileMediaDto;
import org.lonelyproject.backend.dto.PromptDto;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.dto.UserProfileDto;
import org.lonelyproject.backend.security.UserAuth;
import org.lonelyproject.backend.service.FileService;
import org.lonelyproject.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/profile/{userId}")
    public UserProfileDto getUserProfileByUserId(@PathVariable String userId) {
        return userService.getPublicUserProfile(userId);
    }

    @PutMapping("/profile/about")
    public void updateUserProfileAbout(@RequestBody UserProfileDto userProfileDto, @AuthenticationPrincipal UserAuth auth) {
        userService.updateProfileAbout(auth.getId(), userProfileDto.getAbout());
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

    @PostMapping("/profile/gallery/upload")
    public List<ProfileMediaDto> uploadUserMediaToProfile(HttpServletRequest request, @AuthenticationPrincipal UserAuth auth) throws IOException {
        List<UploadedFile> files = fileService.handleFileUpload(request, -1);

        return userService.addMediaToUserProfileGallery(auth.getId(), files);
    }

    @DeleteMapping("/profile/gallery/delete/{mediaId}")
    public void deleteProfileMedia(@PathVariable int mediaId, @AuthenticationPrincipal UserAuth auth) {
        userService.deleteProfileMedia(mediaId, auth.getId());
    }

    @GetMapping("/profile/interests")
    public List<InterestCategoryDto> getInterests() {
        return userService.getInterestsByCategory();
    }

    @PostMapping("/profile/interest")
    public void addInterestToProfile(@RequestBody InterestDto interestDto, @AuthenticationPrincipal UserAuth auth) {
        userService.addInterestToUserProfile(auth.getId(), interestDto);
    }

    @DeleteMapping("/profile/interest/{interestId}")
    public void deleteInterestFromProfile(@PathVariable Integer interestId, @AuthenticationPrincipal UserAuth auth) {
        userService.deleteUserProfileInterest(auth.getId(), interestId);
    }

    @PostMapping("/profile/prompt")
    public void addPromptToProfile(@RequestBody PromptDto promptDto, @AuthenticationPrincipal UserAuth auth) {
        userService.addPromptToUserProfile(auth.getId(), promptDto);
    }

    @DeleteMapping("/profile/prompt/{promptId}")
    public void deletePromptFromProfile(@PathVariable Integer promptId, @AuthenticationPrincipal UserAuth auth) {
        userService.deleteUserProfilePrompt(auth.getId(), promptId);
    }

    @PutMapping("/profile/prompt")
    public void editUserPrompt(@RequestBody PromptDto promptDto, @AuthenticationPrincipal UserAuth auth) {
        userService.editUserPrompt(auth.getId(), promptDto);
    }

    @GetMapping("/profile/prompts")
    public List<PromptDto> getPrompts() {
        return userService.getPrompts();
    }
}
