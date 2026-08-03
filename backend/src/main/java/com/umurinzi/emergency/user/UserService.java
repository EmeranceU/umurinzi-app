package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.exception.ApiException;
import com.umurinzi.emergency.common.exception.ErrorCode;
import com.umurinzi.emergency.common.exception.NotFoundException;
import com.umurinzi.emergency.user.dto.UpdateProfileRequest;
import com.umurinzi.emergency.user.dto.UserProfileResponse;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfilePhotoStorageService profilePhotoStorageService;

    public UserService(UserRepository userRepository, ProfilePhotoStorageService profilePhotoStorageService) {
        this.userRepository = userRepository;
        this.profilePhotoStorageService = profilePhotoStorageService;
    }

    public UserProfileResponse getProfile(UUID userId) {
        return userRepository.findById(userId).map(UserProfileResponse::from).orElseThrow(() -> new NotFoundException(
                "User not found"));
    }

    /**
     * SDD §5.2 — only non-null fields on the request are applied, so a client can
     * update a single field (e.g. just {@code alertMode}) without resending the whole
     * profile. {@code email} isn't editable here — changing it is an identity change,
     * not a profile edit, and isn't in scope yet.
     */
    @Transactional
    public UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }
        if (request.profilePhotoUrl() != null) {
            user.setProfilePhotoUrl(request.profilePhotoUrl());
        }
        if (request.medicalNotes() != null) {
            user.setMedicalNotes(request.medicalNotes());
        }
        if (request.preferredLanguage() != null) {
            user.setPreferredLanguage(request.preferredLanguage());
        }
        if (request.alertMode() != null) {
            user.setAlertMode(parseAlertMode(request.alertMode()));
        }
        if (request.silenceOtherHelpersOnAccept() != null) {
            user.setSilenceOtherHelpersOnAccept(request.silenceOtherHelpersOnAccept());
        }

        return UserProfileResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserProfileResponse updatePhoto(UUID userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setProfilePhotoUrl(profilePhotoStorageService.store(file));
        return UserProfileResponse.from(userRepository.saveAndFlush(user));
    }

    private AlertMode parseAlertMode(String value) {
        try {
            return AlertMode.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "alertMode must be SILENT or AUDIBLE");
        }
    }
}
