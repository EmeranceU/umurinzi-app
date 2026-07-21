const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

/**
 * Metro bundler config. Defaults are sufficient for Phase 0 — override here as
 * specific packages (e.g. SVG transforms) are actually added in later phases.
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
