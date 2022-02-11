package org.lonelyproject.backend.security;

import org.lonelyproject.backend.enums.UserRole;

public class SecurityConstants {

    public static final String HEADER_STRING = "X-Authorization-Firebase";
    public static final String DEFAULT_USER_ROLE = UserRole.ROLE_UNREGISTERED.toString();
    public static final String JWT_ROLE_KEY = "role";

    private SecurityConstants() {

    }
}