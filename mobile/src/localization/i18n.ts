import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import en from './en.json';

/**
 * i18next setup for `preferredLanguage` (SDD — User Profile). Only English is
 * populated so far; French/Kinyarwanda translation files land alongside the screens
 * that need them (Phase 10+).
 */
i18n.use(initReactI18next).init({
  resources: {
    en: { translation: en },
  },
  lng: 'en',
  fallbackLng: 'en',
  interpolation: { escapeValue: false },
});

export default i18n;
