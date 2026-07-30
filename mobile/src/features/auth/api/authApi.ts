import { apiClient } from '@/api/client';
import { AuthUser } from '@/features/auth/store/authStore';

export type TokenResponse = {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  user: AuthUser;
};

type ApiEnvelope<T> = { success: boolean; data: T; error: { code: string; message: string } | null };

export type LoginRequest = { email: string; password: string };

export type RegisterRequest = {
  fullName: string;
  email: string;
  phoneNumber: string;
  password: string;
  preferredLanguage?: string;
};

export async function login(request: LoginRequest): Promise<TokenResponse> {
  const response = await apiClient.post<ApiEnvelope<TokenResponse>>('/auth/login', request);
  return response.data.data;
}

export async function register(request: RegisterRequest): Promise<TokenResponse> {
  const response = await apiClient.post<ApiEnvelope<TokenResponse>>('/auth/register', request);
  return response.data.data;
}
