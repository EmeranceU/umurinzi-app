package com.umurinzi.emergency.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Bootstraps the Firebase Admin SDK for server-side push (SDD §5.7 Notification
 * Module). Credentials path is externalized via {@code app.firebase.credentials-path}
 * (see application.yml / FIREBASE_CREDENTIALS_PATH) so the actual service-account key
 * never lives in source control (SDD §6 — gitignored under
 * {@code src/main/resources/firebase/}).
 *
 * <p>Boots defensively: if no credentials file is present yet (e.g. local dev before
 * a Firebase project exists), this logs a warning and skips initialization rather than
 * failing application startup — nothing in Phase 0 depends on a live {@link
 * FirebaseApp} bean.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${app.firebase.credentials-path}")
    private Resource credentials;

    @Bean
    public FirebaseApp firebaseApp() {
        if (!credentials.exists()) {
            log.warn(
                    "Firebase credentials not found at {} — push notifications will not be available "
                            + "until a real service-account key is supplied (SDD §6).",
                    credentials);
            return null;
        }
        try (InputStream serviceAccount = credentials.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.getApps().isEmpty() ? FirebaseApp.initializeApp(options) : FirebaseApp.getInstance();
        } catch (IOException e) {
            log.error("Failed to initialize Firebase Admin SDK", e);
            return null;
        }
    }
}
