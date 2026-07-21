/**
 * Read-scoped APIs for what a Helper is allowed to see: active alerts, emergency
 * detail, and history (SDD §1.1 Design note, §5.9). Includes {@code
 * HelperAccessGuard}, the single reusable relationship-based authorization check
 * (owner ↔ {@code emergency_contacts.linked_user_id}) reused by the REST controllers
 * here and by {@code realtime.StompChannelInterceptor}. Phase 4 work.
 */
package com.umurinzi.emergency.helper;
