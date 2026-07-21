-- v1.2 addition (SDD Changelog v1.2, §1.4c): Silent Emergency becomes the default.
-- Governs only the owner's own device presentation — never what a Helper receives.

ALTER TABLE users
    ADD COLUMN alert_mode VARCHAR(10) NOT NULL DEFAULT 'SILENT'
        CHECK (alert_mode IN ('SILENT', 'AUDIBLE'));
