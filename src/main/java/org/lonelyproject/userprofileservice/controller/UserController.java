package org.lonelyproject.userprofileservice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.lonelyproject.auth.dto.UserAuth;
import org.lonelyproject.userprofileservice.dto.InterestCategoryDto;
import org.lonelyproject.userprofileservice.dto.InterestDto;
import org.lonelyproject.userprofileservice.dto.ProfileMediaDto;
import org.lonelyproject.userprofileservice.dto.PromptDto;
import org.lonelyproject.userprofileservice.dto.UploadedFile;
import org.lonelyproject.userprofileservice.dto.UserProfileDto;
import org.lonelyproject.userprofileservice.enums.ConnectionStatus;
import org.lonelyproject.userprofileservice.service.FileService;
import org.lonelyproject.userprofileservice.service.MatchingService;
import org.lonelyproject.userprofileservice.service.UserService;
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
    private final MatchingService matchingService;

    public UserController(UserService userService, FileService fileService, MatchingService matchingService) {
        this.userService = userService;
        this.fileService = fileService;
        this.matchingService = matchingService;
    }

    @GetMapping("/profile/{userId}")
    public UserProfileDto getUserProfileByUserId(@PathVariable String userId, @AuthenticationPrincipal UserAuth auth) {
        return userService.getPublicUserProfile(userId, auth.getId());
    }

    @PostMapping("/profile/{userId}/connect")
    public void connectToUser(@PathVariable String userId, @AuthenticationPrincipal UserAuth auth) {
        userService.sendConnectionRequest(auth.getId(), userId);
    }

    @PostMapping("/profile/{userId}/connect/accept")
    public void acceptUserConnectionRequest(@PathVariable String userId, @AuthenticationPrincipal UserAuth auth) {
        userService.changeConnectionStatus(auth.getId(), userId, ConnectionStatus.CONNECTED);
    }

    @PostMapping("/profile/{userId}/connect/deny")
    public void denyUserConnectionRequest(@PathVariable String userId, @AuthenticationPrincipal UserAuth auth) {
        userService.changeConnectionStatus(auth.getId(), userId, ConnectionStatus.DENIED);
    }

    @GetMapping("/profile/connections/pending")
    public List<UserProfileDto> getPendingConnections(@AuthenticationPrincipal UserAuth auth) {
        return userService.getConnectionsByStatus(auth.getId(), ConnectionStatus.PENDING);
    }

    @GetMapping("/profile/connections/connected")
    public List<UserProfileDto> getAcceptedConnections(@AuthenticationPrincipal UserAuth auth) {
        return userService.getConnectionsByStatus(auth.getId(), ConnectionStatus.CONNECTED);
    }

    @GetMapping("/profiles")
    public List<UserProfileDto> getUserProfiles() {
        return userService.getProfiles();
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

    @GetMapping("/profile/matches")
    public List<UserProfileDto> getMatches(@AuthenticationPrincipal UserAuth auth) {
        return matchingService.getMatches(auth.getId());
    }
}
