package org.lonelyproject.backend.security;

import static org.lonelyproject.backend.security.SecurityConstants.DEFAULT_USER_ROLE;
import static org.lonelyproject.backend.security.SecurityConstants.HEADER_STRING;
import static org.lonelyproject.backend.security.SecurityConstants.JWT_ROLE_KEY;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FirebaseAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String xAuth = request.getHeader(HEADER_STRING);
        if (xAuth == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            SecurityContextHolder.getContext().setAuthentication(getAuth(xAuth));

            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            throw new SecurityException(e);
        }
    }

    public static UsernamePasswordAuthenticationToken getAuth(String authHeader) throws FirebaseAuthException {
        FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(authHeader);

        String role = (String) token.getClaims().getOrDefault(JWT_ROLE_KEY, DEFAULT_USER_ROLE);
        UserAuth auth = new UserAuth(token.getUid(), token.getName(), token.isEmailVerified(), role);

        return new UsernamePasswordAuthenticationToken(auth, null, Collections.singleton(new SimpleGrantedAuthority(role)));
    }
}