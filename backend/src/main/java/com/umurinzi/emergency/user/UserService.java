package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.exception.NotFoundException;
import com.umurinzi.emergency.user.dto.UserProfileResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfile(UUID userId) {
        return userRepository.findById(userId).map(UserProfileResponse::from).orElseThrow(() -> new NotFoundException(
                "User not found"));
    }
}
