import axios, { AxiosInstance } from 'axios';
import { API_BASE_URL } from '@/constants/config';
import { useAuthStore } from '@/features/auth/store/authStore';
import { getSession, saveSession, clearSession } from '@/services/storage/SecureStorage';

/**
 * Attach-JWT / refresh-on-401 interceptors (SDD §4). A single in-flight refresh is
 * shared across concurrent 401s (rather than each firing its own refresh call) via
 * `refreshPromise` below.
 */
let refreshPromise: Promise<string | null> | null = null;

async function performRefresh(): Promise<string | null> {
  const stored = await getSession();
  if (!stored) {
    return null;
  }
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
      refreshToken: stored.refreshToken,
    });
    const { accessToken, refreshToken, user } = response.data.data;
    await saveSession({ accessToken, refreshToken, user });
    useAuthStore.getState().setSession(accessToken, user);
    return accessToken;
  } catch {
    await clearSession();
    useAuthStore.getState().clearSession();
    return null;
  }
}

export function attachInterceptors(client: AxiosInstance): void {
  client.interceptors.request.use(config => {
    const token = useAuthStore.getState().accessToken;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  client.interceptors.response.use(
    response => response,
    async error => {
      const original = error.config;
      // Spring Security's default here is 403 Forbidden for a missing/invalid/expired
      // token (no custom AuthenticationEntryPoint configured to make it 401) — treat
      // both as "needs a refresh," not just 401.
      const isAuthFailure = error.response?.status === 401 || error.response?.status === 403;
      if (isAuthFailure && !original._retried) {
        original._retried = true;
        refreshPromise ??= performRefresh().finally(() => {
          refreshPromise = null;
        });
        const newToken = await refreshPromise;
        if (newToken) {
          original.headers.Authorization = `Bearer ${newToken}`;
          return client(original);
        }
      }
      return Promise.reject(error);
    },
  );
}
