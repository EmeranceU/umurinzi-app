package com.umurinzi.emergency.response;

import com.umurinzi.emergency.common.entity.BaseEntity;
import com.umurinzi.emergency.contact.EmergencyContact;
import com.umurinzi.emergency.emergency.EmergencyEvent;
import com.umurinzi.emergency.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * One row per {@code (emergencyEvent, contact)}, what the five Helper actions
 * (Accept / On My Way / Call Victim / Call Police / Mark Safe) read and write (SDD
 * §1.4d, §2.2). Renamed from {@code EmergencyResponse} in v1.3.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "helper_responses",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "uq_helper_responses_event_contact",
                        columnNames = {"emergency_event_id", "contact_id"}))
public class HelperResponse extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emergency_event_id", nullable = false)
    private EmergencyEvent emergencyEvent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private EmergencyContact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responder_user_id")
    private User responderUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ResponseStatus status;

    @Column(name = "is_sharing_location", nullable = false)
    private boolean sharingLocation;

    @Column(name = "police_called", nullable = false)
    private boolean policeCalled;

    @Column(name = "viewed_at")
    private Instant viewedAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Column(name = "on_my_way_at")
    private Instant onMyWayAt;

    @Column(name = "police_called_at")
    private Instant policeCalledAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
