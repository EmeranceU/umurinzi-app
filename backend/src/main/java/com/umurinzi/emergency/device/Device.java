package com.umurinzi.emergency.device;

import com.umurinzi.emergency.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A User's paired SafetyButton(s) (SDD §2.2). {@code bleMacAddress} is unique so the
 * same physical device can't be double-registered to two accounts.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "ble_mac_address", nullable = false, unique = true)
    private String bleMacAddress;

    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "battery_level")
    private Integer batteryLevel;

    @Column(name = "paired_at", nullable = false)
    private Instant pairedAt;

    @Column(name = "last_connected_at")
    private Instant lastConnectedAt;
}
