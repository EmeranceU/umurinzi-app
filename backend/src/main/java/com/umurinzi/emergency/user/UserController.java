package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.dto.ApiResponse;
import com.umurinzi.emergency.security.UserPrincipal;
import com.umurinzi.emergency.user.dto.UpdateProfileRequest;
import com.umurinzi.emergency.user.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** SDD §5.2. All endpoints require a valid access token (see SecurityConfig). */
@Tag(name = "User")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(userService.getProfile(principal.getId()));
    }

    @PatchMapping("/me")
    public ApiResponse<UserProfileResponse> updateMe(
            @AuthenticationPrincipal UserPrincipal principal, @RequestBody UpdateProfileRequest request) {
        return ApiResponse.ok(userService.updateProfile(principal.getId(), request));
    }

    @PostMapping("/me/photo")
    public ApiResponse<UserProfileResponse> updateMyPhoto(
            @AuthenticationPrincipal UserPrincipal principal, @RequestParam("file") MultipartFile file) {
        return ApiResponse.ok(userService.updatePhoto(principal.getId(), file));
    }
}
