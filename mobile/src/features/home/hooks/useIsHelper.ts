/**
 * True if the caller appears as `linked_user_id` on any contact — drives whether the
 * Helper tab is shown (SDD §1.1 Design note, §4). Hardcoded to `false` until the
 * `helper` API module exists (Phase 4/12) to actually answer this.
 */
export function useIsHelper(): boolean {
  return false;
}
