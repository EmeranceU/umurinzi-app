/**
 * TypeScript mirrors of the backend's schema-level enums (SDD §2). Kept in sync by
 * hand for now; DTO request/response shapes get added module-by-module starting
 * Phase 1 alongside the API client calls that use them.
 */

export type TriggerSource = 'BLE_BUTTON' | 'MANUAL_APP';

export type EmergencyStatus = 'ACTIVE' | 'RESOLVED' | 'FALSE_ALARM' | 'CANCELLED';

export type AlertMode = 'SILENT' | 'AUDIBLE';

export type ResponseStatus = 'NOTIFIED' | 'VIEWED' | 'ACCEPTED' | 'ON_MY_WAY';

export type NotificationChannel = 'PUSH' | 'SMS' | 'EMAIL' | 'CALL';
