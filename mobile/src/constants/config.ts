/**
 * Central place for build-time config values. Hardcoded dev defaults for now —
 * Phase 1 wires a real env-var solution (e.g. react-native-config) so these differ
 * per build (dev/staging/prod) instead of being compiled in. See .env.example.
 */
export const API_BASE_URL = 'http://localhost:8080/api/v1';
export const WS_BASE_URL = 'ws://localhost:8080/ws';
