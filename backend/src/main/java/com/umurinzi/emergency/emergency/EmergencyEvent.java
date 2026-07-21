package com.umurinzi.emergency.emergency;

import com.umurinzi.emergency.device.Device;
import com.umurinzi.emergency.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * The core incident record (SDD §2.2). {@code silenceHelpersOnAccept} is snapshotted
 * from {@code User.silenceOtherHelpersOnAccept} at creation time and never re-read
 * from the profile afterward, so changing the preference mid-emergency can't alter an
 * event already in flight (§1.4d).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "emergency_events",
        indexes = {
            @Index(name = "idx_emergency_events_user_status", columnList = "user_id, status"),
            @Index(name = "idx_emergency_events_status_triggered", columnList = "status, triggered_at")
        })
public class EmergencyEvent {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EmergencyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_source", nullable = false, length = 15)
    private TriggerSource triggerSource;

    @Column(name = "silence_helpers_on_accept", nullable = false)
    private boolean silenceHelpersOnAccept;

    @Column(name = "initial_lat", nullable = false, precision = 10, scale = 7)
    private BigDecimal initialLat;

    @Column(name = "initial_lng", nullable = false, precision = 10, scale = 7)
    private BigDecimal initialLng;

    @Column(name = "initial_accuracy")
    private BigDecimal initialAccuracy;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "triggered_at", nullable = false)
    private Instant triggeredAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;
}
