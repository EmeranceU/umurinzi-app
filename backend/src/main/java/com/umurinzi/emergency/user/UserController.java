package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.dto.ApiResponse;
import com.umurinzi.emergency.security.UserPrincipal;
import com.umurinzi.emergency.user.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
