import React from 'react';
import { RefreshControl, ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import type { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { colors, spacing } from '@/theme';
import { useAuthStore } from '@/features/auth/store/authStore';
import { useProfile } from '@/features/profile/api/useProfile';
import { useLogout } from '@/features/auth/api/useLogout';
import { ProfileStackParamList } from '@/app/navigators/ProfileStack';

export function ProfileScreen() {
  const navigation = useNavigation<NativeStackNavigationProp<ProfileStackParamList>>();
  const user = useAuthStore(state => state.user);
  const { isFetching, refetch } = useProfile();
  const logout = useLogout();

  return (
    <ScrollView
      contentContainerStyle={styles.container}
      refreshControl={<RefreshControl refreshing={isFetching} onRefresh={refetch} tintColor={colors.primary} />}>
      <Text style={styles.title}>Profile</Text>

      <View style={styles.card}>
        <Field label="Full name" value={user?.fullName} />
        <Field label="Email" value={user?.email} />
        <Field label="Phone number" value={user?.phoneNumber} />
        <Field label="Preferred language" value={user?.preferredLanguage ?? '—'} />
        <Field label="Medical notes" value={user?.medicalNotes || '—'} multiline />
      </View>

      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Emergency Alert Settings</Text>
        <Field label="Alert mode" value={user?.alertMode === 'AUDIBLE' ? 'Audible' : 'Silent (default)'} />
        <Field
          label="Silence other Helpers on accept"
          value={user?.silenceOtherHelpersOnAccept ? 'On' : 'Off'}
        />
      </View>

      <TouchableOpacity style={styles.editButton} onPress={() => navigation.navigate('EditProfile')}>
        <Text style={styles.editButtonText}>Edit Profile</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.logoutButton} onPress={() => logout.mutate()}>
        <Text style={styles.logoutText}>Log Out</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

function Field({ label, value, multiline }: { label: string; value?: string | null; multiline?: boolean }) {
  return (
    <View style={styles.field}>
      <Text style={styles.fieldLabel}>{label}</Text>
      <Text style={[styles.fieldValue, multiline && styles.fieldValueMultiline]}>{value || '—'}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flexGrow: 1, backgroundColor: colors.background, padding: spacing.lg },
  title: { color: colors.text, fontSize: 28, fontWeight: '700', marginBottom: spacing.lg },
  card: { backgroundColor: colors.surface, borderRadius: 12, padding: spacing.lg, marginBottom: spacing.lg },
  sectionTitle: { color: colors.text, fontSize: 16, fontWeight: '600', marginBottom: spacing.sm },
  field: { marginBottom: spacing.md },
  fieldLabel: { color: colors.textMuted, fontSize: 12, textTransform: 'uppercase', letterSpacing: 0.5 },
  fieldValue: { color: colors.text, fontSize: 16, marginTop: spacing.xs / 2 },
  fieldValueMultiline: { lineHeight: 22 },
  editButton: {
    backgroundColor: colors.primary,
    borderRadius: 8,
    padding: spacing.md,
    alignItems: 'center',
    marginBottom: spacing.md,
  },
  editButtonText: { color: colors.text, fontWeight: '600', fontSize: 16 },
  logoutButton: { borderColor: colors.danger, borderWidth: 1, borderRadius: 8, padding: spacing.md, alignItems: 'center' },
  logoutText: { color: colors.danger, fontWeight: '600' },
});
