# Umurinzi Mobile

React Native / TypeScript app. See [`../docs/SDD.md`](../docs/SDD.md) §4 for the full folder-structure rationale — this README only covers running what's scaffolded so far.

## Status: Phase 0 scaffolding

What exists: `package.json` with every dependency named in the SDD's tech stack, TypeScript/Babel/Metro/ESLint/Prettier config, the full `src/features/*` folder structure with placeholder screens, real navigation wiring (`AuthStack` / `MainTabNavigator` / `EmergencyStack` / `HelperStack` composed in `RootNavigator`), the shared Axios client, Zustand `authStore` shape, i18next setup, and theme tokens.

What does **not** exist yet: any real screen content, the BLE/GPS/alarm/notification services (each is a one-line placeholder module noting which phase implements it), API calls beyond a bare Axios instance, and the native `android/`/`ios/` projects — see their READMEs.

## First-time setup

```bash
npm install
npx @react-native-community/cli init UmurinziMobile --directory /tmp/umurinzi-scaffold
# copy the generated android/ and ios/ into this repo (see android/README.md, ios/README.md)
```

## Running

```bash
npm start          # Metro bundler
npm run android     # separate terminal, once android/ exists
npm run ios         # separate terminal, once ios/ exists
```

Points at the backend via `src/constants/config.ts` (`API_BASE_URL`, `WS_BASE_URL`) — defaults to `http://localhost:8080`, matching the backend's default port.

## Checks

```bash
npm run typecheck
npm run lint
npm test
```
