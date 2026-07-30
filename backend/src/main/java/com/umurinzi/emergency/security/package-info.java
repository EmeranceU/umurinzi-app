/**
 * JWT authentication, {@link com.umurinzi.emergency.security.UserPrincipal}, and RBAC
 * support (SDD §3, §6). {@code @RequireRole}/{@code @CurrentUser} annotations in
 * {@code security.annotation} land module-by-module as needed — not required yet with
 * only one role-agnostic protected endpoint ({@code GET /users/me}) in place.
 */
package com.umurinzi.emergency.security;
