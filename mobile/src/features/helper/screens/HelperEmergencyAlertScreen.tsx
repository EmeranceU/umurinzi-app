import React from 'react';
import { PlaceholderScreen } from '@/components/PlaceholderScreen';

/** Always full-screen, always loud — launched by platform full-screen intent / Critical Alert (SDD §1.4c). Phase 12. */
export function HelperEmergencyAlertScreen() {
  return <PlaceholderScreen title="Helper Emergency Alert" note="Phase 12 (SDD §7)." />;
}
