package org.lonelyproject.auth.config;

import org.lonelyproject.auth.FirebaseAuthorizationFilter;
import org.lonelyproject.auth.RestAccessDeniedHandler;
import org.lonelyproject.auth.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final FirebaseAuthorizationFilter firebaseAuthorizationFilter;

    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
        RestAccessDeniedHandler restAccessDeniedHandler, FirebaseAuthorizationFilter firebaseAuthorizationFilter) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.firebaseAuthorizationFilter = firebaseAuthorizationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .accessDeniedHandler(restAccessDeniedHandler)
            .and()
            .authorizeRequests()
            .antMatchers("/ws/**").permitAll()
            .antMatchers("/admin/**", "/**/admin/**", "/actuator/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(firebaseAuthorizationFilter, BasicAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
