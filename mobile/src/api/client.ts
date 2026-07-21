import axios from 'axios';
import { API_BASE_URL } from '@/constants/config';

/**
 * Shared Axios instance every feature's `*Api.ts` module builds on. JWT attachment
 * and refresh-on-401 (SDD §1.3, §4 `api/interceptors.ts`) are Phase 1 work — they
 * need a working auth module to attach/refresh against, which doesn't exist yet.
 */
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});
