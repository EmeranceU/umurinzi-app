-- v1.3 additions (SDD Changelog v1.3, §1.4d, §2.3): the five Helper actions
-- (Accept, On My Way, Call Victim, Call Police, Mark Safe) and the WebSocket
-- real-time layer's persisted-state side.

-- --- helper_responses: RESPONDING -> ACCEPTED, add ON_MY_WAY, split responded_at ---
ALTER TABLE helper_responses DROP CONSTRAINT helper_responses_status_check;

UPDATE helper_responses SET status = 'ACCEPTED' WHERE status = 'RESPONDING';

ALTER TABLE helper_responses
    ADD COLUMN viewed_at TIMESTAMPTZ,
    ADD COLUMN accepted_at TIMESTAMPTZ,
    ADD COLUMN on_my_way_at TIMESTAMPTZ,
    ADD COLUMN is_sharing_location BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN police_called BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN police_called_at TIMESTAMPTZ;

UPDATE helper_responses SET viewed_at = responded_at WHERE status = 'VIEWED';
UPDATE helper_responses SET accepted_at = responded_at WHERE status = 'ACCEPTED';

ALTER TABLE helper_responses DROP COLUMN responded_at;

ALTER TABLE helper_responses
    ADD CONSTRAINT helper_responses_status_check
        CHECK (status IN ('NOTIFIED', 'VIEWED', 'ACCEPTED', 'ON_MY_WAY'));

ALTER TABLE helper_responses
    ADD CONSTRAINT chk_helper_responses_police
        CHECK (police_called_at IS NOT NULL OR police_called = FALSE),
    ADD CONSTRAINT chk_helper_responses_accepted_at
        CHECK (accepted_at IS NOT NULL OR status NOT IN ('ACCEPTED', 'ON_MY_WAY')),
    ADD CONSTRAINT chk_helper_responses_on_my_way_at
        CHECK (on_my_way_at IS NOT NULL OR status <> 'ON_MY_WAY');

-- --- helper_locations: the Helper-side mirror of emergency_locations --------
CREATE TABLE helper_locations (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    helper_response_id  UUID NOT NULL REFERENCES helper_responses (id) ON DELETE CASCADE,
    latitude            NUMERIC(10, 7) NOT NULL,
    longitude           NUMERIC(10, 7) NOT NULL,
    accuracy            NUMERIC(10, 2),
    speed               NUMERIC(6, 2),
    heading             NUMERIC(5, 2),
    recorded_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_helper_locations_response_recorded ON helper_locations (helper_response_id, recorded_at);

-- --- stand-down-on-accept preference: profile default + per-emergency snapshot ---
ALTER TABLE users
    ADD COLUMN silence_other_helpers_on_accept BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE emergency_events
    ADD COLUMN silence_helpers_on_accept BOOLEAN NOT NULL DEFAULT FALSE;

-- --- notifications: STAND_DOWN type for the soften-other-Helpers broadcast ---
ALTER TABLE notifications DROP CONSTRAINT notifications_type_check;
ALTER TABLE notifications
    ADD CONSTRAINT notifications_type_check
        CHECK (type IN ('EMERGENCY_ALERT', 'STATUS_UPDATE', 'RESPONSE_UPDATE', 'STAND_DOWN', 'SYSTEM'));
