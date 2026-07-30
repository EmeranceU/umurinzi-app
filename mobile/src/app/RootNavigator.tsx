import React, { useEffect, useState } from 'react';
import { ActivityIndicator, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { AuthStack } from '@/app/navigators/AuthStack';
import { MainTabNavigator } from '@/app/navigators/MainTabNavigator';
import { EmergencyStack } from '@/app/navigators/EmergencyStack';
import { HelperStack } from '@/app/navigators/HelperStack';
import { useAuthStore } from '@/features/auth/store/authStore';
import { getSession } from '@/services/storage/SecureStorage';
import { colors } from '@/theme';

export type RootStackParamList = {
  Auth: undefined;
  Main: undefined;
  Emergency: { emergencyId: string };
  Helper: { emergencyId: string };
};

const Stack = createNativeStackNavigator<RootStackParamList>();

/**
 * Top-level switch: unauthenticated -> AuthStack, authenticated -> the main tabs, with
 * EmergencyStack/HelperStack presented modally on top (SDD §4).
 *
 * On mount, restores a previously-saved session from Keychain so closing/reopening
 * the app doesn't log the user out — the access token might be stale by then, but the
 * Axios 401 interceptor (SDD §4 api/interceptors.ts) transparently refreshes it on the
 * next request either way.
 */
export function RootNavigator() {
  const isAuthenticated = useAuthStore(state => state.isAuthenticated);
  const [isRestoring, setIsRestoring] = useState(true);

  useEffect(() => {
    getSession()
      .then(session => {
        if (session) {
          useAuthStore.getState().setSession(session.accessToken, session.user);
        }
      })
      .finally(() => setIsRestoring(false));
  }, []);

  if (isRestoring) {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center', backgroundColor: colors.background }}>
        <ActivityIndicator color={colors.primary} />
      </View>
    );
  }

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
