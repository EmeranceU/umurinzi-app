import * as Keychain from 'react-native-keychain';
import { AuthUser } from '@/features/auth/store/authStore';

/**
 * Session storage via Keychain (iOS) / Keystore-backed encrypted storage (Android) —
 * never AsyncStorage (SDD §4, §6). Stores tokens *and* the profile together so app
 * relaunch can restore the session with one synchronous-feeling read instead of an
 * extra network round trip to `/users/me`.
 */
const SERVICE = 'com.umurinzi.mobile.session';

export type StoredSession = {
  accessToken: string;
  refreshToken: string;
  user: AuthUser;
};

export async function saveSession(session: StoredSession): Promise<void> {
  await Keychain.setGenericPassword('umurinzi', JSON.stringify(session), { service: SERVICE });
}

export async function getSession(): Promise<StoredSession | null> {
  const result = await Keychain.getGenericPassword({ service: SERVICE });
  if (!result) {
    return null;
  }
  try {
    return JSON.parse(result.password) as StoredSession;
  } catch {
    return null;
  }
}

export async function clearSession(): Promise<void> {
  await Keychain.resetGenericPassword({ service: SERVICE });
}
