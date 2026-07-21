import React from 'react';
import { PlaceholderScreen } from '@/components/PlaceholderScreen';

/** AUDIBLE mode only — SILENT mode (the default) shows a discreet toast instead (SDD §1.4c). Phase 8. */
export function EmergencyAlertScreen() {
  return <PlaceholderScreen title="Emergency Alert" note="AUDIBLE mode only. Phase 8 (SDD §7)." />;
}
