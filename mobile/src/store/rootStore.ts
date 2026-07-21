/**
 * Zustand store composition / reset-on-logout (SDD §4). Each feature owns its own
 * slice (e.g. `features/auth/store/authStore.ts`); this module will compose a
 * `resetAllStores()` helper once there's more than one slice with state worth
 * clearing on logout. Phase 6+ work.
 */
export {};
