import React, { useState } from 'react';
import {
  ActivityIndicator,
  ScrollView,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import type { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { colors, spacing } from '@/theme';
import { useAuthStore } from '@/features/auth/store/authStore';
import { useUpdateProfile } from '@/features/profile/api/useUpdateProfile';
import { ProfileStackParamList } from '@/app/navigators/ProfileStack';

export function EditProfileScreen() {
  const navigation = useNavigation<NativeStackNavigationProp<ProfileStackParamList>>();
  const user = useAuthStore(state => state.user);
  const updateProfile = useUpdateProfile();

  const [fullName, setFullName] = useState(user?.fullName ?? '');
  const [phoneNumber, setPhoneNumber] = useState(user?.phoneNumber ?? '');
  const [preferredLanguage, setPreferredLanguage] = useState(user?.preferredLanguage ?? '');
  const [medicalNotes, setMedicalNotes] = useState(user?.medicalNotes ?? '');
  const [alertMode, setAlertMode] = useState<'SILENT' | 'AUDIBLE'>(user?.alertMode ?? 'SILENT');
  const [silenceOnAccept, setSilenceOnAccept] = useState(user?.silenceOtherHelpersOnAccept ?? false);

  const errorMessage =
    (updateProfile.error as any)?.response?.data?.error?.message ??
    (updateProfile.error ? 'Could not save changes' : null);

  const handleSave = () => {
    updateProfile.mutate(
      { fullName, phoneNumber, preferredLanguage, medicalNotes, alertMode, silenceOtherHelpersOnAccept: silenceOnAccept },
      { onSuccess: () => navigation.goBack() },
    );
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Edit Profile</Text>

      <Text style={styles.label}>Full name</Text>
      <TextInput style={styles.input} value={fullName} onChangeText={setFullName} placeholderTextColor={colors.textMuted} />

      <Text style={styles.label}>Phone number</Text>
      <TextInput
        style={styles.input}
        value={phoneNumber}
        onChangeText={setPhoneNumber}
        keyboardType="phone-pad"
        placeholderTextColor={colors.textMuted}
      />

      <Text style={styles.label}>Preferred language</Text>
      <TextInput
        style={styles.input}
        value={preferredLanguage}
        onChangeText={setPreferredLanguage}
        placeholder="en"
        placeholderTextColor={colors.textMuted}
      />

      <Text style={styles.label}>Medical notes</Text>
      <TextInput
        style={[styles.input, styles.textArea]}
        value={medicalNotes}
        onChangeText={setMedicalNotes}
        placeholder="Allergies, conditions, medications a helper should know about"
        placeholderTextColor={colors.textMuted}
        multiline
        numberOfLines={4}
      />

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Emergency Alert Mode</Text>
        <Text style={styles.helpText}>
          Silent: your own phone stays quiet when you trigger an emergency — just a discreet confirmation. Your
          Helpers are still alerted loudly either way. Audible restores a loud alarm on your own phone too.
        </Text>
        <View style={styles.modeRow}>
          <ModePill label="Silent (recommended)" selected={alertMode === 'SILENT'} onPress={() => setAlertMode('SILENT')} />
          <ModePill label="Audible" selected={alertMode === 'AUDIBLE'} onPress={() => setAlertMode('AUDIBLE')} />
        </View>
      </View>

      <View style={styles.switchRow}>
        <View style={styles.switchLabelBlock}>
          <Text style={styles.label}>Silence other Helpers once someone accepts</Text>
          <Text style={styles.helpText}>Softens (not cancels) other Helpers' alarms once one Helper accepts.</Text>
        </View>
        <Switch value={silenceOnAccept} onValueChange={setSilenceOnAccept} trackColor={{ true: colors.primary }} />
      </View>

      {errorMessage ? <Text style={styles.error}>{errorMessage}</Text> : null}

      <TouchableOpacity style={styles.saveButton} disabled={updateProfile.isPending} onPress={handleSave}>
        {updateProfile.isPending ? <ActivityIndicator color={colors.text} /> : <Text style={styles.saveButtonText}>Save</Text>}
      </TouchableOpacity>

      <TouchableOpacity style={styles.cancelButton} onPress={() => navigation.goBack()}>
        <Text style={styles.cancelButtonText}>Cancel</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

function ModePill({ label, selected, onPress }: { label: string; selected: boolean; onPress: () => void }) {
  return (
    <TouchableOpacity style={[styles.pill, selected && styles.pillSelected]} onPress={onPress}>
      <Text style={[styles.pillText, selected && styles.pillTextSelected]}>{label}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: { flexGrow: 1, backgroundColor: colors.background, padding: spacing.lg },
  title: { color: colors.text, fontSize: 24, fontWeight: '700', marginBottom: spacing.lg },
  label: { color: colors.text, fontSize: 14, fontWeight: '600', marginBottom: spacing.xs },
  input: {
    backgroundColor: colors.surface,
    color: colors.text,
    borderRadius: 8,
    padding: spacing.md,
    marginBottom: spacing.md,
  },
  textArea: { minHeight: 90, textAlignVertical: 'top' },
  section: { marginBottom: spacing.lg },
  sectionTitle: { color: colors.text, fontSize: 16, fontWeight: '600', marginBottom: spacing.xs },
  helpText: { color: colors.textMuted, fontSize: 12, lineHeight: 17, marginBottom: spacing.sm },
  modeRow: { flexDirection: 'row', gap: spacing.sm },
  pill: {
    flex: 1,
    borderWidth: 1,
    borderColor: colors.textMuted,
    borderRadius: 999,
    paddingVertical: spacing.sm,
    alignItems: 'center',
  },
  pillSelected: { backgroundColor: colors.primary, borderColor: colors.primary },
  pillText: { color: colors.textMuted, fontWeight: '600', fontSize: 13 },
  pillTextSelected: { color: colors.text },
  switchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: spacing.lg,
  },
  switchLabelBlock: { flex: 1, marginRight: spacing.md },
  error: { color: colors.danger, marginBottom: spacing.md, textAlign: 'center' },
  saveButton: {
    backgroundColor: colors.primary,
    borderRadius: 8,
    padding: spacing.md,
    alignItems: 'center',
    marginBottom: spacing.md,
  },
  saveButtonText: { color: colors.text, fontWeight: '600', fontSize: 16 },
  cancelButton: { alignItems: 'center', padding: spacing.sm },
  cancelButtonText: { color: colors.textMuted, fontSize: 14 },
});
