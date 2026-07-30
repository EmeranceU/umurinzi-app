/**
 * Central place for build-time config values. Hardcoded dev defaults for now —
 * Phase 1 wires a real env-var solution (e.g. react-native-config) so these differ
 * per build (dev/staging/prod) instead of being compiled in. See .env.example.
 */
// NOTE: SDD §5 specifies a `/api/v1` base path; the backend doesn't have that prefix
// wired yet (controllers currently map bare `/auth`, `/users`, etc. — see Phase 1
// notes). Pointed at what actually exists for now; reconcile when the prefix lands.
// Port 8090, not 8080 — the backend's Docker Compose publishes there specifically
// to dodge other local services (e.g. Apache) some dev machines already have bound
// to 8080, which `adb reverse` would otherwise silently route to instead (see the
// comment in ../../docker-compose.yml).
//
// localhost, via `adb reverse tcp:8090 tcp:8090` — single-phone-over-USB setup for
// now. A WiFi-LAN-IP variant (for a second phone, so both can reach the backend
// without needing adb reverse on two devices at once) was tried and works, but adds
// a "phone must be on the same WiFi network" requirement; deferred until the
// two-phone flow is actually being built out. See git history for that version if
// picking it back up.
export const API_BASE_URL = 'http://localhost:8090';
export const WS_BASE_URL = 'ws://localhost:8090/ws';
