/**
 * Minimal placeholder design tokens so early screens don't hardcode hex values.
 * Not a final visual identity — revisit alongside the first real screens (Phase 6+).
 */
export const colors = {
  background: '#0F1417',
  surface: '#1A2226',
  text: '#F4F6F5',
  textMuted: '#9AA6A5',
  primary: '#1C6E63',
  danger: '#B3392C',
  warning: '#C97A2B',
} as const;

export const spacing = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
} as const;
