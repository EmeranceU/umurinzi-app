import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { EmergencyAlertScreen } from '@/features/emergency/screens/EmergencyAlertScreen';
import { EmergencyDetailScreen } from '@/features/emergency/screens/EmergencyDetailScreen';

export type EmergencyStackParamList = {
  EmergencyAlert: { emergencyId: string };
  EmergencyDetail: { emergencyId: string };
};

const Stack = createNativeStackNavigator<EmergencyStackParamList>();

/** Modal-presented from RootNavigator when useTriggerEmergency resolves to AUDIBLE mode (SDD §1.4c). */
export function EmergencyStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false, presentation: 'fullScreenModal' }}>
      <Stack.Screen name="EmergencyAlert" component={EmergencyAlertScreen} />
      <Stack.Screen name="EmergencyDetail" component={EmergencyDetailScreen} />
    </Stack.Navigator>
  );
}
