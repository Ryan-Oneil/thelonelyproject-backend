package org.lonelyproject.userprofileservice.security;

import org.lonelyproject.userprofileservice.enums.UserRole;

public class SecurityConstants {

    public static final String HEADER_STRING = "X-Authorization-Firebase";
    public static final String DEFAULT_USER_ROLE = UserRole.ROLE_UNREGISTERED.toString();
    public static final String JWT_ROLE_KEY = "role";

    private SecurityConstants() {

    }
}