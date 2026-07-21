-- Seed the three roles referenced throughout the SDD (§1.1, §2.2).
-- EMERGENCY_CONTACT is kept for onboarding UX; Helper Dashboard access is actually
-- relationship-derived via emergency_contacts.linked_user_id, not this role (§1.1 Design note).

INSERT INTO roles (name, description) VALUES
    ('USER', 'Owns a SafetyButton device and/or triggers their own emergencies'),
    ('EMERGENCY_CONTACT', 'Onboarding-time self-identification as primarily a contact for someone else'),
    ('ADMIN', 'System-wide monitoring, user management, and audit access');
