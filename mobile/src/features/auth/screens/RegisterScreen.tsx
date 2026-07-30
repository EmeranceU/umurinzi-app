import React, { useState } from 'react';
import { ActivityIndicator, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { colors, spacing } from '@/theme';
import { useRegister } from '@/features/auth/api/useRegister';

export function RegisterScreen() {
  const navigation = useNavigation<any>();
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const register = useRegister();

  const errorMessage =
    (register.error as any)?.response?.data?.error?.message ?? (register.error ? 'Registration failed' : null);

  const canSubmit = fullName && email && phoneNumber && password.length >= 8;

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Create account</Text>

      <TextInput
        style={styles.input}
        placeholder="Full name"
        placeholderTextColor={colors.textMuted}
        value={fullName}
        onChangeText={setFullName}
      />
      <TextInput
        style={styles.input}
        placeholder="Email"
        placeholderTextColor={colors.textMuted}
        autoCapitalize="none"
        keyboardType="email-address"
        value={email}
        onChangeText={setEmail}
      />
      <TextInput
        style={styles.input}
        placeholder="Phone number (e.g. +250700000000)"
        placeholderTextColor={colors.textMuted}
        keyboardType="phone-pad"
        value={phoneNumber}
        onChangeText={setPhoneNumber}
      />
      <TextInput
        style={styles.input}
        placeholder="Password (min. 8 characters)"
        placeholderTextColor={colors.textMuted}
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />

      {errorMessage ? <Text style={styles.error}>{errorMessage}</Text> : null}

      <TouchableOpacity
        style={[styles.button, !canSubmit && styles.buttonDisabled]}
        disabled={register.isPending || !canSubmit}
        onPress={() => register.mutate({ fullName, email, phoneNumber, password, preferredLanguage: 'en' })}>
        {register.isPending ? (
          <ActivityIndicator color={colors.text} />
        ) : (
          <Text style={styles.buttonText}>Register</Text>
        )}
      </TouchableOpacity>

      <TouchableOpacity onPress={() => navigation.navigate('Login')}>
        <Text style={styles.link}>Already have an account? Log in</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flexGrow: 1, justifyContent: 'center', backgroundColor: colors.background, padding: spacing.lg },
  title: { color: colors.text, fontSize: 24, fontWeight: '700', textAlign: 'center', marginBottom: spacing.xl },
  input: {
    backgroundColor: colors.surface,
    color: colors.text,
    borderRadius: 8,
    padding: spacing.md,
    marginBottom: spacing.md,
  },
  error: { color: colors.danger, marginBottom: spacing.md, textAlign: 'center' },
  button: {
    backgroundColor: colors.primary,
    borderRadius: 8,
    padding: spacing.md,
    alignItems: 'center',
    marginTop: spacing.sm,
  },
  buttonDisabled: { opacity: 0.5 },
  buttonText: { color: colors.text, fontWeight: '600', fontSize: 16 },
  link: { color: colors.primary, textAlign: 'center', marginTop: spacing.lg },
});
