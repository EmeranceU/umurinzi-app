import { apiClient } from '@/api/client';
import { AuthUser } from '@/features/auth/store/authStore';

type ApiEnvelope<T> = { success: boolean; data: T; error: { code: string; message: string } | null };

export type UpdateProfileRequest = Partial<{
  fullName: string;
  phoneNumber: string;
  profilePhotoUrl: string;
  medicalNotes: string;
  preferredLanguage: string;
  alertMode: 'SILENT' | 'AUDIBLE';
  silenceOtherHelpersOnAccept: boolean;
}>;

export async function getProfile(): Promise<AuthUser> {
  const response = await apiClient.get<ApiEnvelope<AuthUser>>('/users/me');
  return response.data.data;
}

export async function updateProfile(request: UpdateProfileRequest): Promise<AuthUser> {
  const response = await apiClient.patch<ApiEnvelope<AuthUser>>('/users/me', request);
  return response.data.data;
}

export type PickedPhoto = { uri: string; type: string; name: string };

export async function uploadProfilePhoto(photo: PickedPhoto): Promise<AuthUser> {
  const form = new FormData();
  // React Native's FormData accepts this shape natively even though it doesn't
  // match the DOM File/Blob type FormData.append() expects in a browser.
  form.append('file', { uri: photo.uri, type: photo.type, name: photo.name } as unknown as Blob);

  const response = await apiClient.post<ApiEnvelope<AuthUser>>('/users/me/photo', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data.data;
}
