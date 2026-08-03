package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.exception.ApiException;
import com.umurinzi.emergency.common.exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Saves an uploaded profile photo to local disk and returns the public URL it's
 * served back from (SDD §5.2 POST /users/me/photo). Deliberately local-disk, not
 * object storage (S3/GCS/etc.) — this is a single-instance dev/demo deployment; the
 * volume mount in docker-compose.yml is what makes uploads survive a container
 * restart. Revisit if this ever needs to scale past one backend instance.
 */
@Service
public class ProfilePhotoStorageService {

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
    private static final java.util.Set<String> ALLOWED_CONTENT_TYPES =
            java.util.Set.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadDir;
    private final String publicBaseUrl;

    public ProfilePhotoStorageService(
            @Value("${app.upload.dir}") String uploadDir, @Value("${app.upload.public-base-url}") String publicBaseUrl) {
        this.uploadDir = Path.of(uploadDir);
        this.publicBaseUrl = publicBaseUrl;
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + uploadDir, e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "File exceeds 5MB limit");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "Only JPEG, PNG, or WebP images are allowed");
        }

        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
        String filename = UUID.randomUUID() + extension;

        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, "Failed to store uploaded file");
        }

        return publicBaseUrl + "/uploads/" + filename;
    }
}
