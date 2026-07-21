import { create } from 'zustand';

/**
 * Session state (SDD §4). Login/register/refresh actions that actually populate this
 * are Phase 6 work, wired against the Phase 1 backend auth module — this is just the
 * shape the rest of the app (RootNavigator's auth/main switch) depends on.
 */
type AuthState = {
  isAuthenticated: boolean;
};

export const useAuthStore = create<AuthState>(() => ({
  isAuthenticated: false,
}));
