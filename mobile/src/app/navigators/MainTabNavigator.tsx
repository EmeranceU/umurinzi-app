import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { HomeScreen } from '@/features/home/screens/HomeScreen';
import { ContactsListScreen } from '@/features/contacts/screens/ContactsListScreen';
import { EmergencyHistoryScreen } from '@/features/history/screens/EmergencyHistoryScreen';
import { ProfileStack } from '@/app/navigators/ProfileStack';
import { HelperDashboardScreen } from '@/features/helper/screens/HelperDashboardScreen';
import { useIsHelper } from '@/features/home/hooks/useIsHelper';

export type MainTabParamList = {
  Home: undefined;
  Contacts: undefined;
  History: undefined;
  Helper: undefined;
  Profile: undefined;
};

const Tab = createBottomTabNavigator<MainTabParamList>();

/** Helper tab is shown only when `useIsHelper()` is true (SDD §1.1 Design note, §4). */
export function MainTabNavigator() {
  const isHelper = useIsHelper();

  return (
    <Tab.Navigator screenOptions={{ headerShown: false }}>
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Contacts" component={ContactsListScreen} />
      <Tab.Screen name="History" component={EmergencyHistoryScreen} />
      {isHelper && <Tab.Screen name="Helper" component={HelperDashboardScreen} />}
      <Tab.Screen name="Profile" component={ProfileStack} />
    </Tab.Navigator>
  );
}
