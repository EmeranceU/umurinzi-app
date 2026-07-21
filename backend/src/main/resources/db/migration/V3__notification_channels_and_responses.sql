-- v1.1 additions (SDD Changelog v1.1, §2.3): SMS fallback + Manual SOS + the Helper
-- response workflow's first cut. Table is created directly as `helper_responses`
-- (its final, v1.3 name) since this migration was written after the SDD renamed it —
-- see the note in §2.3.

-- --- notifications: multi-channel + multi-provider support -------------------
ALTER TABLE notifications
    ALTER COLUMN recipient_user_id DROP NOT NULL;

ALTER TABLE notifications
    ADD COLUMN recipient_contact_id UUID REFERENCES emergency_contacts (id) ON DELETE CASCADE,
    ADD COLUMN recipient_phone_number VARCHAR(30),
    ADD COLUMN provider VARCHAR(30),
    ADD COLUMN tracking_token VARCHAR(255),
    ADD COLUMN delivered_at TIMESTAMPTZ;

ALTER TABLE notifications RENAME COLUMN fcm_message_id TO provider_message_id;

ALTER TABLE notifications
    ADD CONSTRAINT chk_notifications_recipient
        CHECK (recipient_user_id IS NOT NULL OR recipient_contact_id IS NOT NULL);

ALTER TABLE notifications DROP CONSTRAINT notifications_type_check;
ALTER TABLE notifications
    ADD CONSTRAINT notifications_type_check
        CHECK (type IN ('EMERGENCY_ALERT', 'STATUS_UPDATE', 'RESPONSE_UPDATE', 'SYSTEM'));

ALTER TABLE notifications DROP CONSTRAINT notifications_channel_check;
ALTER TABLE notifications
    ADD CONSTRAINT notifications_channel_check
        CHECK (channel IN ('PUSH', 'SMS', 'EMAIL', 'CALL'));

CREATE INDEX idx_notifications_recipient_contact_id ON notifications (recipient_contact_id);
CREATE UNIQUE INDEX idx_notifications_tracking_token ON notifications (tracking_token) WHERE tracking_token IS NOT NULL;

-- --- helper_responses (v1.1 shape; expanded further in V5) --------------------
CREATE TABLE helper_responses (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    emergency_event_id  UUID NOT NULL REFERENCES emergency_events (id) ON DELETE CASCADE,
    contact_id          UUID NOT NULL REFERENCES emergency_contacts (id) ON DELETE RESTRICT,
    responder_user_id   UUID REFERENCES users (id) ON DELETE RESTRICT,
    status              VARCHAR(15) NOT NULL DEFAULT 'NOTIFIED'
                            CHECK (status IN ('NOTIFIED', 'VIEWED', 'RESPONDING')),
    responded_at        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_helper_responses_event_contact UNIQUE (emergency_event_id, contact_id)
);

CREATE INDEX idx_helper_responses_event_id ON helper_responses (emergency_event_id);
CREATE INDEX idx_helper_responses_responder_user_id ON helper_responses (responder_user_id);
