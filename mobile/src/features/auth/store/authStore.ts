import { create } from 'zustand';

export type AuthUser = {
  id: string;
  fullName: string;
  email: string;
  phoneNumber: string;
  alertMode: 'SILENT' | 'AUDIBLE';
  role: string;
};

type AuthState = {
  isAuthenticated: boolean;
  accessToken: string | null;
  user: AuthUser | null;
  setSession: (accessToken: string, user: AuthUser) => void;
  clearSession: () => void;
};

/**
 * Session state (SDD §4). Tokens themselves live in Keychain (SecureStorage), not
 * here — this only holds the in-memory access token (for the Axios interceptor to
 * attach) and the profile the UI renders. `RootNavigator` switches between AuthStack
 * and the main app purely off `isAuthenticated`.
 */
export const useAuthStore = create<AuthState>(set => ({
  isAuthenticated: false,
  accessToken: null,
  user: null,
  setSession: (accessToken, user) => set({ isAuthenticated: true, accessToken, user }),
  clearSession: () => set({ isAuthenticated: false, accessToken: null, user: null }),
}));
