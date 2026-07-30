# Umurinzi Emergency Safety Alert System

Arduino Nano 33 BLE Sense Rev2 emergency button → React Native mobile app → Spring Boot backend → PostgreSQL → Emergency Contacts / Helpers.

Full design is in **[docs/SDD.md](docs/SDD.md)** — architecture, ERD, folder structures, API spec, and the implementation plan this build follows. See `backend/README.md` and `mobile/README.md` for what's actually implemented so far versus still scaffolding.

## Repository layout

```
umurinzi-app/
├─ backend/    Spring Boot 3 / Java 25 API (see backend/README.md)
├─ mobile/     React Native / TypeScript app (see mobile/README.md)
├─ docs/       Software Design Document
└─ docker-compose.yml   Postgres + Redis + backend (+ optional pgAdmin)
```

## Running the backend stack locally

```bash
cp .env.example .env   # then fill in real secrets
docker compose up -d postgres redis
cd backend && mvn spring-boot:run   # or: docker compose up -d backend
```

Once the backend is up (port **8090** via Docker Compose, **8080** if run directly with `mvn spring-boot:run` — see the comment in `docker-compose.yml`):

- Swagger UI: http://localhost:8090/swagger-ui.html
- OpenAPI JSON: http://localhost:8090/v3/api-docs
- Health check: http://localhost:8090/actuator/health

Optional pgAdmin: `docker compose --profile tools up -d pgadmin` → http://localhost:5050

## Running the mobile app

See [mobile/README.md](mobile/README.md) — the native `android/` and `ios/` projects are generated via the React Native CLI, not hand-scaffolded in this repo.

## Status

Phase 0 scaffolding is done. Phase 1 Auth (register/login/refresh/logout, JWT, a protected `/users/me`) is implemented on the backend and wired end-to-end into the mobile Login/Register screens. See `backend/README.md` / `mobile/README.md` for details, and `docs/SDD.md` §7 for what's next.
