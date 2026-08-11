import { useQuery } from '@tanstack/react-query';
import * as profileApi from '@/features/profile/api/profileApi';
import { useAuthStore } from '@/features/auth/store/authStore';

export const PROFILE_QUERY_KEY = ['profile', 'me'] as const;

/**
 * Refetches the caller's own profile from the backend and keeps `authStore.user` (and
 * the Keychain-persisted session) in sync — so a profile edited elsewhere (or on a
 * previous app run) is reflected here, not just whatever the login response returned.
 */
export function useProfile() {
  const isAuthenticated = useAuthStore(state => state.isAuthenticated);
  const accessToken = useAuthStore(state => state.accessToken);

  return useQuery({
    queryKey: PROFILE_QUERY_KEY,
    queryFn: async () => {
      const user = await profileApi.getProfile();
      if (accessToken) {
        useAuthStore.getState().setSession(accessToken, user);
      }
      return user;
    },
    enabled: isAuthenticated,
  });
}
