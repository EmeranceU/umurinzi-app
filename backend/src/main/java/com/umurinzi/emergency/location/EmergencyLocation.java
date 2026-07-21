package com.umurinzi.emergency.location;

import com.umurinzi.emergency.emergency.EmergencyEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Append-only track log for an event, populated by the owner's device every 15s while
 * {@code ACTIVE} (SDD §2.2). Retained indefinitely for now — part of the incident
 * record.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "emergency_locations",
        indexes = @Index(name = "idx_emergency_locations_event_recorded", columnList = "emergency_event_id, recorded_at"))
public class EmergencyLocation {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emergency_event_id", nullable = false)
    private EmergencyEvent emergencyEvent;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column
    private BigDecimal accuracy;

    @Column
    private BigDecimal speed;

    @Column
    private BigDecimal heading;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
