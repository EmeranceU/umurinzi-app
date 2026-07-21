import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { AuthStack } from '@/app/navigators/AuthStack';
import { MainTabNavigator } from '@/app/navigators/MainTabNavigator';
import { EmergencyStack } from '@/app/navigators/EmergencyStack';
import { HelperStack } from '@/app/navigators/HelperStack';
import { useAuthStore } from '@/features/auth/store/authStore';

export type RootStackParamList = {
  Auth: undefined;
  Main: undefined;
  Emergency: { emergencyId: string };
  Helper: { emergencyId: string };
};

const Stack = createNativeStackNavigator<RootStackParamList>();

/**
 * Top-level switch: unauthenticated -> AuthStack, authenticated -> the main tabs, with
 * EmergencyStack/HelperStack presented modally on top (SDD §4). Wiring is real; what
 * populates `isAuthenticated` and what actually triggers the modal presentations is
 * Phase 6+ work.
 */
export function RootNavigator() {
  const isAuthenticated = useAuthStore(state => state.isAuthenticated);

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {isAuthenticated ? (
          <>
            <Stack.Screen name="Main" component={MainTabNavigator} />
            <Stack.Screen name="Emergency" component={EmergencyStack} options={{ presentation: 'fullScreenModal' }} />
            <Stack.Screen name="Helper" component={HelperStack} options={{ presentation: 'fullScreenModal' }} />
          </>
        ) : (
          <Stack.Screen name="Auth" component={AuthStack} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
