package com.umurinzi.emergency.contact;

import com.umurinzi.emergency.common.entity.BaseEntity;
import com.umurinzi.emergency.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A contact entry owned by the protected user (SDD §2.2). {@code linkedUser} is
 * populated once the contact's phone/email matches a registered account — that's the
 * flag that upgrades a contact from "SMS-only" to "Helper" (§1.1 Design note).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "emergency_contacts",
        indexes = @Index(name = "idx_contacts_owner_priority", columnList = "owner_user_id, priority_order"))
public class EmergencyContact extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_user_id")
    private User linkedUser;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column
    private String relationship;

    @Column(name = "priority_order", nullable = false)
    private int priorityOrder;

    @Column(name = "notify_via_push", nullable = false)
    private boolean notifyViaPush;

    @Column(name = "notify_via_sms", nullable = false)
    private boolean notifyViaSms;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
