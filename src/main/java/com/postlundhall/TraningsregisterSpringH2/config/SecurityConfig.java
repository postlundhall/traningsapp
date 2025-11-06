package com.postlundhall.TraningsregisterSpringH2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for managing security aspects including authorization, login-logout, and CSRF protection.
 * @author postlundhall
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures Spring Security settings by defining a security filter chain
     * comprising authorization, form-based login, logout functionality, CSRF protection, and H2 console headers.
     * Public access is permitted to safe specific endpoints (e.g., /register, /login).
     * Other endpoints require authentication.
     *
     * @param http the HttpSecurity object used to configure web-based security
     * @return a SecurityFilterChain that defines the security configuration
     * @throws Exception if an error occurs during security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/error/**", "/h2-console/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ovningar", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        return http.build();
    }
}