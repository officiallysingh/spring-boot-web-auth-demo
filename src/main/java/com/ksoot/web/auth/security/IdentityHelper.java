package com.ksoot.web.auth.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.List;

/**
 * @author Rajveer Singh
 */
public class IdentityHelper {

    public static final String SYSTEM_USER = "SYSTEM";

    public enum ClaimName {

        //@formatter:off
        USERNAME("unique_name"),
        SUBJECT("sub");
        //@formatter:on

        private final String value;

        ClaimName(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    public static Authentication getAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication;
        } else {
            throw new InsufficientAuthenticationException("Authentication required");
        }
    }

    public static Principal getPrinciple() {
        return (Principal) getAuthentication().getPrincipal();
    }

    public static String getPrincipalName() {
        return getAuthentication().getName();
    }

    public static JwtAuthenticationToken getJwtAuthenticationToken() {
        return (JwtAuthenticationToken) getAuthentication();
    }

    public static String getJwtString() {
        return getJwtAuthenticationToken().getToken().getTokenValue();
    }

    public static String getAuthorizationHeader(final String claimName) {
        return "Bearer" + getJwtAuthenticationToken().getToken().getClaimAsString(claimName);
    }

    public static List<String> getAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        } else {
            throw new InsufficientAuthenticationException("Authentication required");
        }
    }

    public static String getClaim(final String claimName) {
        return getJwtAuthenticationToken().getToken().getClaimAsString(claimName);
    }

    public static String getClaim(final ClaimName claimName) {
        return getJwtAuthenticationToken().getToken().getClaimAsString(claimName.value);
    }

    public static String getLoginName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        } else {
            throw new InsufficientAuthenticationException("Authentication required");
        }
    }

    public static String getUsername() {
        return getClaim(ClaimName.USERNAME.value());
    }

    public static String getAuditUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        } else {
            return SYSTEM_USER;
        }
    }

    private IdentityHelper() {
        throw new IllegalStateException("Just a utility class, not supposed to be instantiated");
    }
}
