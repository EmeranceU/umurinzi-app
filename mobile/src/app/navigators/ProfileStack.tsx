import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { ProfileScreen } from '@/features/profile/screens/ProfileScreen';
import { EditProfileScreen } from '@/features/profile/screens/EditProfileScreen';

export type ProfileStackParamList = {
  ProfileHome: undefined;
  EditProfile: undefined;
};

const Stack = createNativeStackNavigator<ProfileStackParamList>();

/** Nested under the Profile tab (SDD §4) — MedicalInfoScreen folded into EditProfile (single field, not worth a separate hop). */
export function ProfileStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ProfileHome" component={ProfileScreen} />
      <Stack.Screen name="EditProfile" component={EditProfileScreen} />
    </Stack.Navigator>
  );
}
