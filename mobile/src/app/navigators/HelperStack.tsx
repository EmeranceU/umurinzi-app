import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { HelperEmergencyAlertScreen } from '@/features/helper/screens/HelperEmergencyAlertScreen';
import { HelperHistoryScreen } from '@/features/helper/screens/HelperHistoryScreen';

export type HelperStackParamList = {
  HelperEmergencyAlert: { emergencyId: string };
  HelperHistory: undefined;
};

const Stack = createNativeStackNavigator<HelperStackParamList>();

/** Modal-presented from RootNavigator on a HELPER_ALARM push (SDD §1.4c). */
export function HelperStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false, presentation: 'fullScreenModal' }}>
      <Stack.Screen name="HelperEmergencyAlert" component={HelperEmergencyAlertScreen} />
      <Stack.Screen name="HelperHistory" component={HelperHistoryScreen} />
    </Stack.Navigator>
  );
}
