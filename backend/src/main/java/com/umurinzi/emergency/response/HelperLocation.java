package com.umurinzi.emergency.response;

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
 * The Helper-side mirror of {@code location.EmergencyLocation} — a Helper's own
 * position while {@code ON_MY_WAY} (SDD §1.4d, §2.2). Scoped to {@code
 * helper_response_id} rather than directly to the emergency, since more than one
 * Helper can be {@code ON_MY_WAY} simultaneously and each needs their own track.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "helper_locations",
        indexes = @Index(name = "idx_helper_locations_response_recorded", columnList = "helper_response_id, recorded_at"))
public class HelperLocation {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "helper_response_id", nullable = false)
    private HelperResponse helperResponse;

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
