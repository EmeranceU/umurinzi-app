import { useMutation } from '@tanstack/react-query';
import { apiClient } from '@/api/client';
import { useAuthStore } from '@/features/auth/store/authStore';
import { clearSession, getSession } from '@/services/storage/SecureStorage';

export function useLogout() {
  return useMutation({
    mutationFn: async () => {
      const stored = await getSession();
      if (stored) {
        // Best-effort — revokes server-side so the refresh token can't be replayed.
        await apiClient.post('/auth/logout', { refreshToken: stored.refreshToken }).catch(() => {});
      }
      await clearSession();
    },
    onSuccess: () => useAuthStore.getState().clearSession(),
  });
}
