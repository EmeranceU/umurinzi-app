import axios from 'axios';
import { API_BASE_URL } from '@/constants/config';
import { attachInterceptors } from '@/api/interceptors';

/** Shared Axios instance every feature's `*Api.ts` module builds on (SDD §4). */
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});

attachInterceptors(apiClient);
