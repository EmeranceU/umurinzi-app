/**
 * WebSocket/STOMP live-update layer (SDD §1.4d, §3, §5.8a): {@code
 * StompChannelInterceptor} (JWT-on-CONNECT + per-topic authorization) and {@code
 * RealtimeEventPublisher}, which only ever relays domain events other modules already
 * persisted — this layer never originates data. Broker/endpoint registration itself
 * lives in {@code config.WebSocketConfig} (present from Phase 0). Phase 4b work.
 */
package com.umurinzi.emergency.realtime;
