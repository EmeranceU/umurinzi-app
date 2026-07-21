# Umurinzi Backend

Spring Boot 3.5 / Java 25 API. See [`../docs/SDD.md`](../docs/SDD.md) for the full design — this README only covers running what's scaffolded so far.

## Status: Phase 0 scaffolding

What exists: build config, package structure for every module in SDD §3, JPA entities + Spring Data repositories matching the ERD (§2), Flyway migrations V1–V5 reproducing the schema's documented version history, security/Swagger/CORS/Redis/Firebase/WebSocket config wiring, and Docker packaging.

What does **not** exist yet: any controller, service, or business logic (auth flows, the emergency state machine, notification dispatch, BLE device registration, etc.). Those land starting Phase 1 — see `docs/SDD.md` §7. Package-level Javadoc (`package-info.java`) in the not-yet-implemented packages notes which phase owns them.

## Running locally

Prerequisites: JDK 25, Maven, and either Docker (for Postgres/Redis) or local instances of both.

```bash
cp ../.env.example ../.env   # from repo root, then fill in real values
docker compose -f ../docker-compose.yml up -d postgres redis
mvn spring-boot:run
```

The app reads `SPRING_PROFILES_ACTIVE` (defaults to `dev`, which points at `localhost` Postgres/Redis). Flyway runs automatically on startup.

- Swagger UI: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

## Running the full stack in Docker

```bash
docker compose -f ../docker-compose.yml up -d --build
```

## Tests

```bash
mvn test
```

`EmergencyApplicationTests` spins up a real Postgres via Testcontainers and verifies every Flyway migration applies cleanly — the only test meaningful at this phase, since there's no business logic yet to unit test.

## Firebase & SMS credentials

Neither is required to boot the app (both fail open with a warning in Phase 0 — nothing consumes them yet):

- Firebase: download the service-account JSON from your Firebase project and place it at `src/main/resources/firebase/service-account.json` (gitignored).
- SMS: set `AFRICAS_TALKING_*` or `TWILIO_*` in `.env` per `docs/SDD.md` §8 (provider choice still open).
