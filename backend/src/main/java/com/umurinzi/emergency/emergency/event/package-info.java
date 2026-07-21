/**
 * Spring application events published by {@code EmergencyService} ({@code
 * EmergencyCreatedEvent}, {@code EmergencyStatusChangedEvent}) and consumed
 * independently by {@code NotificationDispatcher}, {@code AuditEventListener}, and
 * (from Phase 4b) {@code realtime.RealtimeEventPublisher} (SDD §3). Phase 2 work.
 */
package com.umurinzi.emergency.emergency.event;
