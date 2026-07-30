import React, { useState } from 'react';
import { ActivityIndicator, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { colors, spacing } from '@/theme';
import { useLogin } from '@/features/auth/api/useLogin';

export function LoginScreen() {
  const navigation = useNavigation<any>();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const login = useLogin();

  const errorMessage =
    (login.error as any)?.response?.data?.error?.message ?? (login.error ? 'Login failed' : null);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Umurinzi</Text>
      <Text style={styles.subtitle}>Sign in</Text>

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
        placeholder="Password"
        placeholderTextColor={colors.textMuted}
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />

      {errorMessage ? <Text style={styles.error}>{errorMessage}</Text> : null}

      <TouchableOpacity
        style={styles.button}
        disabled={login.isPending || !email || !password}
        onPress={() => login.mutate({ email, password })}>
        {login.isPending ? <ActivityIndicator color={colors.text} /> : <Text style={styles.buttonText}>Log In</Text>}
      </TouchableOpacity>

      <TouchableOpacity onPress={() => navigation.navigate('Register')}>
        <Text style={styles.link}>Don't have an account? Register</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', backgroundColor: colors.background, padding: spacing.lg },
  title: { color: colors.text, fontSize: 32, fontWeight: '700', textAlign: 'center' },
  subtitle: { color: colors.textMuted, fontSize: 16, textAlign: 'center', marginTop: spacing.xs, marginBottom: spacing.xl },
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
  buttonText: { color: colors.text, fontWeight: '600', fontSize: 16 },
  link: { color: colors.primary, textAlign: 'center', marginTop: spacing.lg },
});
