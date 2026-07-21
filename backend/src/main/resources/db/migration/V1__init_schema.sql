-- Umurinzi Emergency Safety Alert System — baseline schema (SDD §2, v1.0 shape).
-- Later versioned migrations (V3-V5) layer on the v1.1-v1.3 additions documented
-- in docs/SDD.md §2.3.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ROLES ----------------------------------------------------------------------
CREATE TABLE roles (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- USERS ------------------------------------------------------------------------
CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id             UUID NOT NULL REFERENCES roles (id) ON DELETE RESTRICT,
    full_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone_number        VARCHAR(30) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    profile_photo_url   VARCHAR(500),
    medical_notes       TEXT,
    preferred_language  VARCHAR(10),
    status              VARCHAR(10) NOT NULL DEFAULT 'ACTIVE'
                            CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DELETED')),
    email_verified      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_users_role_id ON users (role_id);

-- REFRESH_TOKENS --------------------------------------------------------------
CREATE TABLE refresh_tokens (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    token_hash  VARCHAR(255) NOT NULL,
    device_info VARCHAR(255),
    ip_address  VARCHAR(45),
    revoked     BOOLEAN NOT NULL DEFAULT FALSE,
    issued_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX idx_refresh_tokens_token_hash ON refresh_tokens (token_hash);

-- DEVICES ------------------------------------------------------------------
CREATE TABLE devices (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id            UUID NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    device_name        VARCHAR(255) NOT NULL,
    ble_mac_address    VARCHAR(17) NOT NULL UNIQUE,
    device_type        VARCHAR(30) NOT NULL DEFAULT 'SAFETY_BUTTON',
    firmware_version   VARCHAR(30),
    is_active          BOOLEAN NOT NULL DEFAULT TRUE,
    battery_level      INTEGER,
    paired_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_connected_at  TIMESTAMPTZ
);

CREATE INDEX idx_devices_user_id ON devices (user_id);

-- EMERGENCY_CONTACTS -----------------------------------------------------------
CREATE TABLE emergency_contacts (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_user_id   UUID NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    linked_user_id  UUID REFERENCES users (id) ON DELETE RESTRICT,
    name            VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(30) NOT NULL,
    relationship    VARCHAR(100),
    priority_order  INTEGER NOT NULL DEFAULT 0,
    notify_via_push BOOLEAN NOT NULL DEFAULT TRUE,
    notify_via_sms  BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_contacts_owner_priority ON emergency_contacts (owner_user_id, priority_order);
CREATE INDEX idx_contacts_linked_user_id ON emergency_contacts (linked_user_id);

-- EMERGENCY_EVENTS ------------------------------------------------------------
CREATE TABLE emergency_events (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    device_id        UUID REFERENCES devices (id) ON DELETE RESTRICT,
    status           VARCHAR(15) NOT NULL DEFAULT 'ACTIVE'
                         CHECK (status IN ('ACTIVE', 'RESOLVED', 'FALSE_ALARM', 'CANCELLED')),
    trigger_source   VARCHAR(15) NOT NULL
                         CHECK (trigger_source IN ('BLE_BUTTON', 'MANUAL_APP')),
    initial_lat      NUMERIC(10, 7) NOT NULL,
    initial_lng      NUMERIC(10, 7) NOT NULL,
    initial_accuracy NUMERIC(10, 2),
    notes            TEXT,
    triggered_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    resolved_at      TIMESTAMPTZ,
    resolved_by      UUID REFERENCES users (id) ON DELETE RESTRICT
);

CREATE INDEX idx_emergency_events_user_status ON emergency_events (user_id, status);
CREATE INDEX idx_emergency_events_status_triggered ON emergency_events (status, triggered_at);

-- EMERGENCY_LOCATIONS -----------------------------------------------------
CREATE TABLE emergency_locations (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    emergency_event_id  UUID NOT NULL REFERENCES emergency_events (id) ON DELETE CASCADE,
    latitude            NUMERIC(10, 7) NOT NULL,
    longitude           NUMERIC(10, 7) NOT NULL,
    accuracy            NUMERIC(10, 2),
    speed               NUMERIC(6, 2),
    heading             NUMERIC(5, 2),
    recorded_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_emergency_locations_event_recorded ON emergency_locations (emergency_event_id, recorded_at);

-- NOTIFICATIONS (base v1.0 shape — expanded in V3) -----------------------------
CREATE TABLE notifications (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    emergency_event_id  UUID REFERENCES emergency_events (id) ON DELETE CASCADE,
    recipient_user_id   UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    type                VARCHAR(20) NOT NULL
                            CHECK (type IN ('EMERGENCY_ALERT', 'STATUS_UPDATE', 'SYSTEM')),
    channel             VARCHAR(10) NOT NULL
                            CHECK (channel IN ('PUSH', 'SMS', 'EMAIL')),
    title               VARCHAR(255) NOT NULL,
    body                TEXT,
    fcm_message_id      VARCHAR(255),
    status              VARCHAR(10) NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING', 'SENT', 'DELIVERED', 'FAILED', 'READ')),
    sent_at             TIMESTAMPTZ,
    read_at             TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_notifications_recipient_user_id ON notifications (recipient_user_id);
CREATE INDEX idx_notifications_emergency_event_id ON notifications (emergency_event_id);

-- FCM_TOKENS -----------------------------------------------------------------
CREATE TABLE fcm_tokens (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token         VARCHAR(500) NOT NULL,
    platform      VARCHAR(10) NOT NULL CHECK (platform IN ('ANDROID', 'IOS')),
    last_used_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_fcm_tokens_token ON fcm_tokens (token);
CREATE INDEX idx_fcm_tokens_user_id ON fcm_tokens (user_id);

-- AUDIT_LOGS -------------------------------------------------------------------
CREATE TABLE audit_logs (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_user_id  UUID REFERENCES users (id) ON DELETE RESTRICT,
    action         VARCHAR(100) NOT NULL,
    entity_type    VARCHAR(100),
    entity_id      UUID,
    ip_address     VARCHAR(45),
    user_agent     TEXT,
    metadata       JSONB,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_logs_actor_user_id ON audit_logs (actor_user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at);
