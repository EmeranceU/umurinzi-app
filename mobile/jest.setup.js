/**
 * react-native-screens' native Fabric codegen (`codegenNativeComponent`) doesn't run
 * under Jest's mocked react-native. Newer react-native-screens releases dropped the
 * `react-native-screens/mock` entry react-navigation's docs historically pointed at,
 * so this is a minimal inline stand-in covering what react-navigation actually needs
 * at import time (enableScreens/Screen/ScreenContainer). Revisit once react-navigation
 * or react-native-screens ship an official Jest mock again for this RN version.
 */
jest.mock('react-native-screens', () => ({
  enableScreens: jest.fn(),
  enableFreeze: jest.fn(),
  screensEnabled: jest.fn(() => false),
  Screen: 'Screen',
  ScreenContainer: 'ScreenContainer',
  ScreenStack: 'ScreenStack',
  ScreenStackHeaderConfig: 'ScreenStackHeaderConfig',
  NativeScreen: 'NativeScreen',
  NativeScreenContainer: 'NativeScreenContainer',
}));
