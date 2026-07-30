import { useMutation } from '@tanstack/react-query';
import * as authApi from '@/features/auth/api/authApi';
import { useAuthStore } from '@/features/auth/store/authStore';
import { saveSession } from '@/services/storage/SecureStorage';

export function useLogin() {
  return useMutation({
    mutationFn: authApi.login,
    onSuccess: async result => {
      await saveSession(result);
      useAuthStore.getState().setSession(result.accessToken, result.user);
    },
  });
}
