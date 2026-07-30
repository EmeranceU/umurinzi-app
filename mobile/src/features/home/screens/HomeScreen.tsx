import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { colors, spacing } from '@/theme';
import { useAuthStore } from '@/features/auth/store/authStore';
import { useLogout } from '@/features/auth/api/useLogout';

export function HomeScreen() {
  const user = useAuthStore(state => state.user);
  const logout = useLogout();

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Umurinzi</Text>
      <View style={styles.card}>
        <Text style={styles.label}>Logged in as</Text>
        <Text style={styles.value}>{user?.fullName}</Text>
        <Text style={styles.valueMuted}>{user?.email}</Text>
        <Text style={styles.valueMuted}>{user?.phoneNumber}</Text>
        <Text style={styles.valueMuted}>Alert mode: {user?.alertMode}</Text>
      </View>
      <Text style={styles.note}>Device status card + manual SOS button — Phase 8 (SDD §7).</Text>
      <TouchableOpacity style={styles.logoutButton} onPress={() => logout.mutate()}>
        <Text style={styles.logoutText}>Log Out</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background, padding: spacing.lg, justifyContent: 'center' },
  title: { color: colors.text, fontSize: 28, fontWeight: '700', textAlign: 'center', marginBottom: spacing.xl },
  card: { backgroundColor: colors.surface, borderRadius: 12, padding: spacing.lg, marginBottom: spacing.lg },
  label: { color: colors.textMuted, fontSize: 12, textTransform: 'uppercase', letterSpacing: 0.5 },
  value: { color: colors.text, fontSize: 20, fontWeight: '600', marginTop: spacing.xs },
  valueMuted: { color: colors.textMuted, fontSize: 14, marginTop: spacing.xs / 2 },
  note: { color: colors.textMuted, fontSize: 13, textAlign: 'center', marginBottom: spacing.xl },
  logoutButton: {
    borderColor: colors.danger,
    borderWidth: 1,
    borderRadius: 8,
    padding: spacing.md,
    alignItems: 'center',
  },
  logoutText: { color: colors.danger, fontWeight: '600' },
});
