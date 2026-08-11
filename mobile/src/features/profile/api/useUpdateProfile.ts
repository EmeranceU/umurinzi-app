import { useMutation, useQueryClient } from '@tanstack/react-query';
import * as profileApi from '@/features/profile/api/profileApi';
import { PROFILE_QUERY_KEY } from '@/features/profile/api/useProfile';
import { useAuthStore } from '@/features/auth/store/authStore';
import { getSession, saveSession } from '@/services/storage/SecureStorage';

export function useUpdateProfile() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: profileApi.updateProfile,
    onSuccess: async user => {
      const accessToken = useAuthStore.getState().accessToken;
      if (accessToken) {
        useAuthStore.getState().setSession(accessToken, user);
      }
      // Re-save the persisted session with the updated profile, keeping whatever
      // refresh token is already stored — this mutation never touches tokens.
      const stored = await getSession();
      if (stored) {
        await saveSession({ ...stored, user });
      }
      queryClient.setQueryData(PROFILE_QUERY_KEY, user);
    },
  });
}
