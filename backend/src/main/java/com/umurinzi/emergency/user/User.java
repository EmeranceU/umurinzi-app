package com.umurinzi.emergency.user;

import com.umurinzi.emergency.common.entity.BaseEntity;
import com.umurinzi.emergency.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * One row per account (SDD §2.2). {@code alertMode} and
 * {@code silenceOtherHelpersOnAccept} govern only this account's own device/emergency
 * behavior (§1.4c–d) — never what a Helper receives.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "medical_notes", columnDefinition = "text")
    private String medicalNotes;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_mode", nullable = false, length = 10)
    private AlertMode alertMode; // DB default 'SILENT' — see V4__user_alert_mode.sql

    @Column(name = "silence_other_helpers_on_accept", nullable = false)
    private boolean silenceOtherHelpersOnAccept;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserStatus status;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
